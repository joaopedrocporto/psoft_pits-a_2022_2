package com.ufcg.psoft.commerce.service.PedidoService;

import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.exception.ClienteException.ClienteIdInvalidoException;
import com.ufcg.psoft.commerce.exception.ClienteException.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoCancelamentoInvalidoException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoNaoExisteException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoEditarPadraoService implements PedidoEditarService  {
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    ModelMapper modelMapper;

    public Pedido editar(Long pedidoId, String clienteCodigoAcesso, PedidoPostPutRequestDTO pedidoPostPutRequestDTO)
    throws
            PedidoNaoExisteException,
            ClienteNaoExisteException,
            CodigoDeAcessoInvalidoException
    {
        this.validarId(pedidoId);

        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);
        Cliente cliente = clienteRepository.findById(pedido.getClienteId()).orElseThrow(ClienteNaoExisteException::new);

        this.validarCodigoAcesso(cliente.getCodigoAcesso(), clienteCodigoAcesso);
        validarAlteracao(pedido.getStatusEntrega());

        modelMapper.map(pedidoPostPutRequestDTO, pedido);
        return pedidoRepository.save(pedido);
    }

    private void validarAlteracao(String status) {
        if (
            status.equals("Pedido pronto") ||
            status.equals("Pedido em rota") ||
            status.equals("Pedido entregue")
        ) throw new PedidoCancelamentoInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoAtual) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoAtual)) throw new CodigoDeAcessoInvalidoException();
    }

    private void validarId(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new ClienteIdInvalidoException();
    }
}
