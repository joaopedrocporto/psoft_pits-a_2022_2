package com.ufcg.psoft.commerce.service.PedidoService;

import com.ufcg.psoft.commerce.exception.ClienteException.ClienteIdInvalidoException;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorNaoExisteException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoNaoEntregueException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoNaoExisteException;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.AssociacaoRepository;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class PedidoExcluirPorEstabelecimentoPadraoService implements PedidoExcluirPorEstabelecimentoService{
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    AssociacaoRepository associacaoRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    EntregadorRepository entregadorRepository;

    public void excluir(Long pedidoId, Long estabelecimentoId, String codigoDeAcesso)
            throws
            EstabelecimentoIdInvalidoException,
            PedidoNaoExisteException,
            CodigoDeAcessoInvalidoException
    {
        this.validarId(estabelecimentoId);

        if (FuncoesValidacao.validarId(pedidoId)) {
            this.validarAtores(pedidoId, estabelecimentoId);
            verificarMotorista(pedidoId, estabelecimentoId);
        } else if (pedidoId == null) {
            this.validarAtores(estabelecimentoId, codigoDeAcesso);
            excluirPedidos(pedidoRepository.retornarPedidosEstabelecimento(estabelecimentoId));
        } else {
            throw new PedidoIdInvalidoException();
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
            ) {estabelecimento.addEntregador(entregador);}
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
    private void validarPedidoEntregue(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);

        if (!Objects.equals(pedido.getStatusEntrega(), "Pedido Entregue")) throw new PedidoNaoEntregueException();
    }

    private  void validarAtores(Long pedidoId, Long estabelecimentoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);
        Estabelecimento estabelecimentoPedido = estabelecimentoRepository.findById(pedido.getEstabelecimentoId()).orElseThrow(EstabelecimentoNaoExisteException::new);
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(EstabelecimentoNaoExisteException::new);

        if (!(estabelecimentoPedido.getCodigoAcesso().equals(estabelecimento.getCodigoAcesso()))) {
            throw new CodigoDeAcessoInvalidoException();
        }
    }

    private  void validarAtores(Long estabelecimentoId, String codigoAcesso) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(EstabelecimentoNaoExisteException::new);

        if (!(estabelecimento.getCodigoAcesso().equals(codigoAcesso))) {
            throw new CodigoDeAcessoInvalidoException();
        }
    }

    private void validarId(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new EstabelecimentoIdInvalidoException();
    }
}