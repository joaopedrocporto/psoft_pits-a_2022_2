package com.ufcg.psoft.commerce.service.PedidoService.PedidoEstabelecimentoService;

import com.ufcg.psoft.commerce.exception.ClienteException.ClienteIdInvalidoException;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorNaoExisteException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoConsultadoNaoExisteException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoNaoExisteException;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.AssociacaoRepository;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class EstabelecimentoExcluirPedidoPadraoService implements EstabelecimentoExcluirPedidoService {
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    AssociacaoRepository associacaoRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    public void excluir(Long pedidoId, Long estabelecimentoId, String codigoAcesso)
            throws
            PedidoNaoExisteException,
            PedidoConsultadoNaoExisteException,
            EstabelecimentoNaoEncontradoException,
            CodigoDeAcessoInvalidoException
    {
        validarIdEstabelecimento(estabelecimentoId);

        this.validarCodigoAcesso(estabelecimentoId, codigoAcesso);

        if (FuncoesValidacao.isNull(pedidoId)) {
            excluirPedidos(pedidoRepository.retornarPedidosEstabelecimento(estabelecimentoId));
        } else {
            validarIdPedido(pedidoId);
            verificarMotorista(pedidoId, estabelecimentoId);
        }
    }

    private void verificarMotorista(Long pedidoId, Long estabelecimentoId) {
        Pedido pedido = this.pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);
        if (!pedido.getEstabelecimentoId().equals(estabelecimentoId)) throw new CodigoDeAcessoInvalidoException();

        if (
                pedido.getStatusEntrega().equals("Pedido em rota") &&
                        FuncoesValidacao.isNotNull(pedido.getEntregadorId())
        ) {
            Entregador entregador = this.entregadorRepository.findById(pedido.getEntregadorId())
                    .orElseThrow(EntregadorNaoExisteException::new);
            entregador.setDisponibilidade(true);
            this.entregadorRepository.save(entregador);
            disponibilizarEntregador(entregador);
        }

        this.pedidoRepository.deleteById(pedidoId);
    }

    private void excluirPedidos(List<Long> pedidosIds) {
        if (!pedidosIds.isEmpty()) {

            LinkedList<Entregador> entregadoresDisponiveis = new LinkedList<>();

            for (Long pedidoId: pedidosIds) {

                Pedido pedido = this.pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);

                if (
                        pedido.getStatusEntrega().equals("Pedido em rota") &&
                                FuncoesValidacao.isNotNull(pedido.getEntregadorId())
                ) {
                    Entregador entregador = this.entregadorRepository.findById(pedido.getEntregadorId())
                            .orElseThrow(EntregadorNaoExisteException::new);
                    entregador.setDisponibilidade(true);
                    this.entregadorRepository.save(entregador);
                    entregadoresDisponiveis.add(entregador);
                }
                this.pedidoRepository.deleteById(pedidoId);
            }
            disponibilizarEntregadores(entregadoresDisponiveis);
        }
    }

    private void disponibilizarEntregador(Entregador entregador) {

        List<Estabelecimento> estabelecimentos = this.estabelecimentoRepository.findAllById(
                this.associacaoRepository
                        .retornarEstabelecimentosComAssociacaoEntregador(entregador.getId())
        );

        for (Estabelecimento estabelecimento : estabelecimentos) {
            if (
                    associacaoRepository.retornarAssociacao(entregador.getId(), estabelecimento.getId()).isStatus()
            ) {
                estabelecimento.addEntregador(entregador);
            }
        }
        this.estabelecimentoRepository.saveAll(estabelecimentos);
    }

    private void disponibilizarEntregadores(LinkedList<Entregador> entregadores) {
        Set<Estabelecimento> estabelecimentosSet = new HashSet<>();
        while (!entregadores.isEmpty()) {
            Entregador entregador = entregadores.removeLast();

            List<Estabelecimento> estabelecimentos = this.estabelecimentoRepository.findAllById(
                    this.associacaoRepository
                            .retornarEstabelecimentosComAssociacaoEntregador(entregador.getId())
            );

            for (Estabelecimento estabelecimento : estabelecimentos) {
                if (
                        associacaoRepository.retornarAssociacao(entregador.getId(), estabelecimento.getId()).isStatus()
                ) {
                    estabelecimento.addEntregador(entregador);
                    estabelecimentosSet.add(estabelecimento);
                }
            }
        }
        this.estabelecimentoRepository.saveAll(estabelecimentosSet);
    }

    private void validarIdPedido(Long pedidoId) {
        if (!FuncoesValidacao.validarId(pedidoId)) throw new PedidoIdInvalidoException();
    }

    private void validarPedidoEstabelecimento(Long estabelecimentoId, Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);

        if (!pedido.getEstabelecimentoId().equals(estabelecimentoId)) {
            throw new CodigoDeAcessoInvalidoException();
        }
    }

    private void validarCodigoAcesso(Long estabelecimentoId, String codigoAcesso) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId)
                .orElseThrow(EstabelecimentoNaoEncontradoException::new);

        if (!UtilCodigoAcesso.validarCodigo(estabelecimento.getCodigoAcesso(), codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }
    }

    private void validarIdEstabelecimento(Long idEstabelecimento) {
        if (!FuncoesValidacao.validarId(idEstabelecimento)) throw new EstabelecimentoIdInvalidoException();
    }

}
