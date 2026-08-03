package com.ufcg.psoft.commerce.service.PedidoService;

import com.ufcg.psoft.commerce.exception.ClienteException.ClienteIdInvalidoException;
import com.ufcg.psoft.commerce.exception.ClienteException.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoCancelamentoInvalidoException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoNaoExisteException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoCancelarPorEstabelecimentoPadraoService implements PedidoCancelarPorEstabelecimentoService {
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    public void cancelar(Long estabelecimentoId, Long pedidoId)
            throws
            PedidoNaoExisteException,
            EstabelecimentoNaoExisteException,
            CodigoDeAcessoInvalidoException,
            EstabelecimentoIdInvalidoException
    {
        this.validarId(pedidoId);

        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);

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

    private void validarId(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new EstabelecimentoIdInvalidoException();
    }
}
