package com.ufcg.psoft.commerce.service.PedidoService.PedidoClienteService;

import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorResponseDTO;
import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoGetRequestDTO;
import com.ufcg.psoft.commerce.exception.ClienteException.ClienteIdInvalidoException;
import com.ufcg.psoft.commerce.exception.ClienteException.ClienteIdsDiferentesException;
import com.ufcg.psoft.commerce.exception.ClienteException.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoNaoExisteException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteVisualizarPedidoPadraoService implements ClienteVisualizarPedidoService{

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public PedidoGetRequestDTO visualizar(Long pedidoId,
                                          Long clienteId ,
                                          String codigoAcesso) {
        validarIdCliente(clienteId);
        validarIdPedido(pedidoId);
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(PedidoNaoExisteException::new);
        Cliente cliente  = clienteRepository.findById(clienteId)
                .orElseThrow(ClienteNaoExisteException::new);
        validarCodigoAcesso(cliente,codigoAcesso);
        checaIdPedidoCliente(clienteId,pedido.getClienteId());
        return modelMapper.map(pedido, PedidoGetRequestDTO.class);
    }

    private void validarIdCliente(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new ClienteIdInvalidoException();
    }
    private void validarIdPedido(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new PedidoIdInvalidoException();
    }

    private void validarCodigoAcesso(Cliente cliente, String codigoAcesso) {
        if (!cliente.getCodigoAcesso().equals(codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }
    }

    private void checaIdPedidoCliente(Long id, Long pedidoIdCliente){
        if (pedidoIdCliente.equals(id)){
            throw new ClienteIdsDiferentesException();
        }
    }
}
