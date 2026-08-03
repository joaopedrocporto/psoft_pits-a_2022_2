package com.ufcg.psoft.commerce.service.PedidoService.PedidoClienteService;

import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoGetRequestDTO;
import com.ufcg.psoft.commerce.exception.ClienteException.ClienteIdInvalidoException;
import com.ufcg.psoft.commerce.exception.ClienteException.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CLienteFiltrarPedidosPorStatusPadraoService implements  ClienteFiltrarPedidosPorStatusService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PedidoRepository pedidoRepository;


    public List<PedidoGetRequestDTO> filtrarPorStatus(Long idCliente, String statusEntrega, String codigoAcessoCliente)
    throws
            ClienteNaoExisteException,
            ClienteIdInvalidoException,
            CodigoDeAcessoInvalidoException
    {
        this.validarId(idCliente);

        Cliente cliente = this.clienteRepository.findById(idCliente).orElseThrow(ClienteNaoExisteException::new);
        this.validarCodigoAcesso(cliente, codigoAcessoCliente);

        List<Long> id_pedidos = pedidoRepository.retornarPedidosClientePorStatusEntrega(idCliente, statusEntrega);

        List<Pedido> pedidos = pedidoRepository.findAllById(id_pedidos);

        List<PedidoGetRequestDTO> pedidosDTO = pedidos.stream()
                .map(PedidoGetRequestDTO::new)
                .collect(Collectors.toList());

        return pedidosDTO;
    }

    private void validarId(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new ClienteIdInvalidoException();
    }

    private void validarCodigoAcesso(Cliente cliente, String codigoAcesso) {
        if (!cliente.getCodigoAcesso().equals(codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }
    }
}