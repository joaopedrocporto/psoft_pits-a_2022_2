package com.ufcg.psoft.commerce.service.PedidoService;

import com.ufcg.psoft.commerce.exception.ClienteException.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoNaoExisteException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoListarPorAmbosPadraoService implements PedidoListarPorAmbosService {
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    public List<Pedido> listar(Long clienteId, Long estabelecimentoId, Long pedidoId)
            throws
            ClienteNaoExisteException,
            EstabelecimentoNaoExisteException,
            PedidoNaoExisteException,
            CodigoDeAcessoInvalidoException {
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(ClienteNaoExisteException::new);
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(EstabelecimentoNaoExisteException::new);

        if (pedidoId != null && pedidoId > 0) {
            this.validarCodigoAcessoCliente(pedidoId, cliente);
            this.validarCodigoAcessoEstabelecimento(pedidoId, estabelecimento);
            Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);
            return List.of(pedido);
        }

        List<Pedido> pedidos = pedidoRepository.findAll()
                .stream()
                .filter(item -> item.getClienteId().equals(clienteId))
                .collect(Collectors.toList());
        return pedidos;
    }

    private void validarCodigoAcessoCliente(Long pedidoId, Cliente cliente) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);
        Cliente pedidoCliente = clienteRepository.findById(pedido.getClienteId())
                .orElseThrow(ClienteNaoExisteException::new);

        if (!(UtilCodigoAcesso.validarCodigo(cliente.getCodigoAcesso(), pedidoCliente.getCodigoAcesso()))) {
            if (!cliente.getCodigoAcesso().equals(pedidoCliente.getCodigoAcesso())) {
                throw new CodigoDeAcessoInvalidoException();
            }
        }
    }

    private void validarCodigoAcessoEstabelecimento(Long pedidoId, Estabelecimento estabelecimento) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);
        Estabelecimento pedidoEstabelecimento = estabelecimentoRepository.findById(pedido.getEstabelecimentoId())
                .orElseThrow(EstabelecimentoNaoExisteException::new);

        if (!(UtilCodigoAcesso.validarCodigo(estabelecimento.getCodigoAcesso(), pedidoEstabelecimento.getCodigoAcesso()))) {
            if (!estabelecimento.getCodigoAcesso().equals(pedidoEstabelecimento.getCodigoAcesso())) {
                throw new CodigoDeAcessoInvalidoException();
            }
        }
    }
}
