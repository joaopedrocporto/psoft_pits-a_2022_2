package com.ufcg.psoft.commerce.service.EstabelecimentoService;

import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorNaoExisteException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoNaoExisteException;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.AssociacaoRepository;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.service.AssociacaoService.AssociacaoDeletarPorIdService;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class EstabelecimentoExcluirPadraoService implements EstabelecimentoExcluirService {
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    AssociacaoRepository associacaoRepository;
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    AssociacaoDeletarPorIdService associacaoDeletarPorIdService;


    public void excluir(Long id, String codidoAcesso) throws EstabelecimentoIdInvalidoException,
            EstabelecimentoNaoEncontradoException, CodigoDeAcessoInvalidoException
    {
        validarId(id);

        Estabelecimento estabelecimento = this.estabelecimentoRepository
                .findById(id)
                .orElseThrow(EstabelecimentoNaoEncontradoException::new);

        validarCodigoAcesso(estabelecimento.getCodigoAcesso(), codidoAcesso);
        removerAssociacoes(this.associacaoRepository.findAllIdsByEstabelecimentoId(estabelecimento.getId()));
        excluirPedidos(pedidoRepository.retornarPedidosEstabelecimento(estabelecimento.getId()));
        this.estabelecimentoRepository.deleteById(id);
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
            disponibilizarEntregador(entregadoresDisponiveis);
        }
    }

    private void disponibilizarEntregador(LinkedList<Entregador> entregadores) {
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

    private void validarId(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new EstabelecimentoIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
    }

    private void removerAssociacoes(List<Long> associacoesIds) {
        if (!associacoesIds.isEmpty()) this.associacaoRepository.deleteAllById(associacoesIds);
    }

}