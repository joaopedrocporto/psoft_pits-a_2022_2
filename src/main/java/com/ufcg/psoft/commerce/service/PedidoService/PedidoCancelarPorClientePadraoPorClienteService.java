package com.ufcg.psoft.commerce.service.PedidoService;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoCancelarPorClientePadraoPorClienteService implements PedidoCancelarPorClienteService {
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    ClienteRepository clienteRepository;

    public void cancelar(Long pedidoId, String clienteCodigoAcesso)
            throws
            PedidoNaoExisteException,
            ClienteNaoExisteException,
            CodigoDeAcessoInvalidoException,
            ClienteIdInvalidoException
    {
        this.validarId(pedidoId);

        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);
        Cliente cliente = clienteRepository.findById(pedido.getClienteId()).orElseThrow(ClienteNaoExisteException::new);

        this.validarCodigoAcesso(cliente.getCodigoAcesso(), clienteCodigoAcesso);
        this.validarCancelamento(pedido.getStatusEntrega());

        this.pedidoRepository.deleteById(pedidoId);
    }

    private void validarCancelamento(String status) {
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
