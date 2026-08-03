package com.ufcg.psoft.commerce.service.PedidoService.PedidoEstabelecimentoService;

import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoConsultadoNaoExisteException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoNaoExisteException;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstabelecimentoListarPedidoPadraoService implements EstabelecimentoListarPedidoService {
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    public List<Pedido> listar(Long pedidoId, Long estabelecimentoId, String codigoAcesso)
        throws
            EstabelecimentoNaoEncontradoException,
            PedidoNaoExisteException,
            CodigoDeAcessoInvalidoException
    {
        this.validarCodigoAcesso(estabelecimentoId, codigoAcesso);

        if (pedidoId == null) {
            // retorna todos os pedidos do estabelecimento
            List<Pedido> pedidos = new ArrayList<>();
            pedidoRepository.findAll().forEach(pedido -> {
                if (pedido.getEstabelecimentoId().equals(estabelecimentoId)) {
                    pedidos.add(pedido);
                }
            });
            return pedidos;
        } else {
            this.validarPedidoEstabelecimento(estabelecimentoId, pedidoId);

            Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);
            return List.of(pedido);
        }
    }

    private void validarPedidoEstabelecimento(Long estabelecimentoId, Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);

        if (!pedido.getEstabelecimentoId().equals(estabelecimentoId)) {
            throw new PedidoConsultadoNaoExisteException();
        }
    }

    private void validarCodigoAcesso(Long estabelecimentoId, String codigoAcesso) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId)
                .orElseThrow(EstabelecimentoNaoEncontradoException::new);

        if (!UtilCodigoAcesso.validarCodigo(estabelecimento.getCodigoAcesso(), codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }
    }
}
