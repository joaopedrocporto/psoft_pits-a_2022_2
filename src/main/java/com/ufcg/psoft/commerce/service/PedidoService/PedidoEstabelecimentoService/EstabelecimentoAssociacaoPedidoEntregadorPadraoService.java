package com.ufcg.psoft.commerce.service.PedidoService.PedidoEstabelecimentoService;

import com.ufcg.psoft.commerce.exception.ClienteException.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorDisponibilidadeInvalidaException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorNaoExisteException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoNaoExisteException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoStatusInvalidoException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.*;
import com.ufcg.psoft.commerce.service.EstabelecimentoService.EstabelecimentoNotificarPedidoNaoPodeSerAtribuidoService;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EstabelecimentoAssociacaoPedidoEntregadorPadraoService implements EstabelecimentoAssociacaoPedidoEntregadorService {
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    AssociacaoRepository associacaoRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    EstabelecimentoNotificarPedidoNaoPodeSerAtribuidoService notificarPedidoServie;

    @Override
    public Pedido associarPedidoEntregador(Long idPedido, Long idEstabelecimeneto, String codigoAcesso) {
        validarIds(idPedido, idEstabelecimeneto);

        Estabelecimento estabelecimento = this.estabelecimentoRepository
                .findById(idEstabelecimeneto)
                .orElseThrow(EstabelecimentoNaoExisteException::new);

        validarCodigoAcesso(estabelecimento.getCodigoAcesso(), codigoAcesso);

        Pedido pedido = this.pedidoRepository
                .findById(idPedido)
                .orElseThrow(PedidoNaoExisteException::new);

        validarStatusPedido(pedido.getStatusEntrega());

        pedido.setEntregadorId(atribuirEntregaPedido(estabelecimento, pedido));
        pedido.setStatusEntrega("Pedido em rota");
        this.notificarCliente(pedido);

        return this.pedidoRepository.save(pedido);
    }

    private String notificarCliente(Pedido pedido) {
        Entregador entregador = this.entregadorRepository.findById(pedido.getEntregadorId())
                .orElseThrow(EntregadorNaoExisteException::new);
        Cliente cliente = this.clienteRepository.findById(pedido.getClienteId())
                .orElseThrow(ClienteNaoExisteException::new);
        return "Olá " + cliente.getNome() + " Seu pedido saiu para entrega!\n" +
                "Nome do entregador: " + entregador.getNome() + "\n" +
                "Tipo de veículo: " + entregador.getTipoVeiculo() + "\n" +
                "Placa do veículo: " + entregador.getPlacaVeiculo() + "\n" +
                "Cor do veículo: " + entregador.getCorVeiculo();
    }

    private Long atribuirEntregaPedido(Estabelecimento estabelecimento, Pedido pedido) {
        if (!estabelecimento.verificarEntregadores()) {
            notificarPedidoServie.notificarClientePedidoNaoPodeSerAtribuido(pedido.getId());
            throw new EntregadorDisponibilidadeInvalidaException();
        } else {
            Entregador entregador = estabelecimento.atribuirEntregador();
            entregador.setDisponibilidade(false);
            this.entregadorRepository.save(entregador);
            indisponibilizarEntregador(entregador);
            estabelecimento.removerPedido(pedido);
            this.estabelecimentoRepository.save(estabelecimento);
            return entregador.getId();
        }
    }

    private void indisponibilizarEntregador(Entregador entregador) {

        List<Estabelecimento> estabelecimentos = this.estabelecimentoRepository.findAllById(
                this.associacaoRepository
                        .retornarEstabelecimentosComAssociacaoEntregador(entregador.getId())
        );
        for (Estabelecimento estabelecimento : estabelecimentos) {
            if (
                    associacaoRepository.retornarAssociacao(entregador.getId(), estabelecimento.getId()).isStatus()
            ) {estabelecimento.removerEntregador(entregador);}
        }
        this.estabelecimentoRepository.saveAll(estabelecimentos);
    }

    private void validarStatusPedido(String statusEntrega) {
        if (!statusEntrega.equals("Pedido pronto")) throw new PedidoStatusInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoAtual) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoAtual)) throw new CodigoDeAcessoInvalidoException();
    }

    private void validarIds(Long idPedido, Long idEstabelecimento) {
        if (!FuncoesValidacao.validarId(idPedido)) throw new PedidoIdInvalidoException();

        if (!FuncoesValidacao.validarId(idEstabelecimento)) throw new EstabelecimentoIdInvalidoException();
    }

}