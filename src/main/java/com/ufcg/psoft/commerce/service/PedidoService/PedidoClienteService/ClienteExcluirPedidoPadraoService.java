package com.ufcg.psoft.commerce.service.PedidoService.PedidoClienteService;

import com.ufcg.psoft.commerce.exception.ClienteException.ClienteIdInvalidoException;
import com.ufcg.psoft.commerce.exception.ClienteException.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoNaoEntregueException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoNaoExisteException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteExcluirPedidoPadraoService implements ClienteExcluirPedidoService {
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    ClienteRepository clienteRepository;

    public void excluir(Long pedidoId, Long clienteId, String codigoAcessoCliente)
        throws
            ClienteIdInvalidoException,
            PedidoNaoExisteException,
            CodigoDeAcessoInvalidoException,
            PedidoNaoEntregueException
    {
        this.validarId(clienteId);

        if (FuncoesValidacao.validarId(pedidoId)) {
            this.validarAtores(pedidoId, clienteId, codigoAcessoCliente);
            this.validarPedidoEntregue(pedidoId);
            pedidoRepository.deleteById(pedidoId);
        } else if (FuncoesValidacao.isNull(pedidoId)) {
            this.validarAtores(clienteId, codigoAcessoCliente);
            pedidoRepository.findAll().forEach(item -> {
                if (item.getClienteId().equals(clienteId)) pedidoRepository.deleteById(item.getId());

            });
        } else {
            throw new ClienteIdInvalidoException();
        }
    }

    private void validarPedidoEntregue(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);

        if (pedido.getStatusEntrega() != "Pedido Entregue") throw new PedidoNaoEntregueException();
    }

    private  void validarAtores(Long pedidoId, Long clienteId, String codigoAcessoCliente) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);
        Cliente clientePedido = clienteRepository.findById(pedido.getClienteId()).orElseThrow(ClienteNaoExisteException::new);
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(ClienteNaoExisteException::new);

        if (!UtilCodigoAcesso.validarCodigo(clientePedido.getCodigoAcesso(), codigoAcessoCliente) &&
            UtilCodigoAcesso.validarCodigo(cliente.getCodigoAcesso(), clientePedido.getCodigoAcesso()))
        {throw new CodigoDeAcessoInvalidoException();}
    }

    private  void validarAtores(Long clienteId, String codigoAcessoCliente) {
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(ClienteNaoExisteException::new);

        if (!UtilCodigoAcesso.validarCodigo(cliente.getCodigoAcesso(), codigoAcessoCliente)) {throw new CodigoDeAcessoInvalidoException();}
    }

    private void validarId(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new ClienteIdInvalidoException();
    }
}
