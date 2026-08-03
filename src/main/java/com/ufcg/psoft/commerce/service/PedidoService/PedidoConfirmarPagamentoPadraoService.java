package com.ufcg.psoft.commerce.service.PedidoService;

import com.ufcg.psoft.commerce.exception.ClienteException.ClienteIdInvalidoException;
import com.ufcg.psoft.commerce.exception.ClienteException.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoNaoExisteException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoPagamentoInvalidoException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PedidoConfirmarPagamentoPadraoService implements PedidoConfirmarPagamentoService {
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    ModelMapper modelMapper;

    public Pedido confirmarPagamento(Long pedidoId, String metodoPagamento, String clienteCodigoAcesso) throws
            PedidoIdInvalidoException, PedidoPagamentoInvalidoException, PedidoNaoExisteException,
            ClienteNaoExisteException, CodigoDeAcessoInvalidoException
    {
        this.validarId(pedidoId);
        this.validarMetodoPagamento(metodoPagamento.toUpperCase());

        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);
        Cliente cliente = clienteRepository.findById(pedido.getClienteId()).orElseThrow(ClienteNaoExisteException::new);

        this.validarCodigoAcesso(cliente.getCodigoAcesso(), clienteCodigoAcesso);
        this.verificarStatusEntrega(pedido.getStatusEntrega());

        pedido.setStatusPagamento(true);
        pedido.setPreco(novoPreco(metodoPagamento, pedido.getPreco()));
        pedido.setStatusEntrega("Pedido em preparo");

        return this.pedidoRepository.save(pedido);
    }

    private Double novoPreco(String metodoPagamento, Double preco) {
        if (metodoPagamento.equals("PIX")) {
            return preco * 0.95;
        } else if (metodoPagamento.equals("DEBITO")) {
            return preco * 0.975;
        } else {return preco;}
    }

    private void verificarStatusEntrega(String statusEntrega) {
        if (!statusEntrega.toUpperCase().equals("PEDIDO RECEBIDO")) throw new ClienteIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoAtual) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoAtual)) throw new CodigoDeAcessoInvalidoException();
    }

    private void validarId(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new PedidoIdInvalidoException();
    }

    private void validarMetodoPagamento(String metodoPagamento){
        if (!FuncoesValidacao.isNotEmptyOrNull(metodoPagamento)) throw new PedidoPagamentoInvalidoException();
        if(
            !(metodoPagamento.equals("PIX") ||
             metodoPagamento.equals("CREDITO") ||
             metodoPagamento.equals("DEBITO"))
        ) {throw new PedidoPagamentoInvalidoException();}
    }
}
