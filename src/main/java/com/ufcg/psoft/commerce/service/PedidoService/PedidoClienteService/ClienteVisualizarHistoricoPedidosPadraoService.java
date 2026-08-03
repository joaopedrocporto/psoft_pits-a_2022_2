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
public class ClienteVisualizarHistoricoPedidosPadraoService implements  ClienteVisualizarHistoricoPedidosService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PedidoRepository pedidoRepository;

    public List<PedidoGetRequestDTO> visualizarHistorico(Long idCliente, String codigoAcessoCliente)
    throws
            ClienteNaoExisteException,
            ClienteIdInvalidoException,
            CodigoDeAcessoInvalidoException
    {
        this.validarId(idCliente);

        Cliente cliente = this.clienteRepository.findById(idCliente).orElseThrow(ClienteNaoExisteException::new);
        this.validarCodigoAcesso(cliente, codigoAcessoCliente);

        // As duas consultas a seguir já retornam os pedidos do mais recente para o mais antigo
        List<Long> idPedidosEntregues = pedidoRepository.retornarPedidosEntregues(idCliente);
        List<Long> idPedidos = pedidoRepository.retornarPedidosNaoEntregues(idCliente);

        idPedidos.addAll(idPedidosEntregues);

        List<Pedido> pedidos = this.pedidoRepository.findAllById(idPedidos);

        List<PedidoGetRequestDTO> pedidosDTO = pedidos.stream()
                .map(PedidoGetRequestDTO::new)
                .collect(Collectors.toList());

        return  pedidosDTO;
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
