package com.ufcg.psoft.commerce.service.ClienteService;

import com.ufcg.psoft.commerce.exception.ClienteException.ClienteIdInvalidoException;
import com.ufcg.psoft.commerce.exception.ClienteException.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorNaoExisteException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoNaoExisteException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.*;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional
public class ClienteDeletarPadraoService implements ClienteDeletarService {

    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    AssociacaoRepository associacaoRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired EntregadorRepository entregadorRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public void deletar(Long IdRemove, String codigoAcesso) throws
            ClienteNaoExisteException,
            CodigoDeAcessoInvalidoException
    {
        validarId(IdRemove);
        Cliente cliente = this.clienteRepository.findById(IdRemove).orElseThrow(ClienteNaoExisteException::new);
        validarCodigoAcesso(cliente.getCodigoAcesso(), codigoAcesso);
        excluirPedidos(this.pedidoRepository.retornarPedidosCliente(cliente.getId()));
        this.clienteRepository.delete(cliente);
    }

    private void validarId(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new ClienteIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
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

}
