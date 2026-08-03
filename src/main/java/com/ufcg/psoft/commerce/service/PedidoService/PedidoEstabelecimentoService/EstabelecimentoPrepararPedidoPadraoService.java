package com.ufcg.psoft.commerce.service.PedidoService.PedidoEstabelecimentoService;

import com.ufcg.psoft.commerce.exception.ClienteException.ClienteIdInvalidoException;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoNaoExisteException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoStatusInvalidoException;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EstabelecimentoPrepararPedidoPadraoService implements EstabelecimentoPrepararPedidoService {
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    EstabelecimentoNotificarClientePedidoProntoService estabelecimentoNotificarClientePedidoProntoService;
    @Autowired
    EstabelecimentoAssociacaoPedidoEntregadorService estabelecimentoAssociacaoPedidoEntregadorService;
    @Override
    public Pedido prepararPedido(
            Long idPedido,
            Long idEstabelecimento,
            String codigoAcesso
    ) throws PedidoIdInvalidoException, EstabelecimentoNaoExisteException, PedidoNaoExisteException,
            CodigoDeAcessoInvalidoException, PedidoStatusInvalidoException {

        validarIds(idPedido, idEstabelecimento);

        Pedido pedido = this.pedidoRepository
                .findById(idPedido)
                .orElseThrow(PedidoNaoExisteException::new);

        Estabelecimento estabelecimento = this.estabelecimentoRepository
                .findById(idEstabelecimento)
                .orElseThrow(EstabelecimentoNaoExisteException::new);

        validarCodigoAcesso(estabelecimento.getCodigoAcesso(), codigoAcesso);
        validarStatusPedido(pedido.getStatusEntrega());
        pedido.setStatusEntrega("Pedido pronto");

        estabelecimento.addPedido(pedido);
        this.estabelecimentoRepository.save(estabelecimento);
        estabelecimentoNotificarClientePedidoProntoService.notificarClientePedidoPronto(pedido.getId());
        this.estabelecimentoAssociacaoPedidoEntregadorService.associarPedidoEntregador(pedido.getId(),estabelecimento.getId(),codigoAcesso);
        return this.pedidoRepository.save(pedido);
    }

    private void validarStatusPedido(String statusEntrega) {
        if (!statusEntrega.equals("Pedido em preparo")) throw new PedidoStatusInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoAtual) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoAtual)) throw new CodigoDeAcessoInvalidoException();
    }

    private void validarIds(Long idPedido, Long idEstabelecimento) {
        if (!FuncoesValidacao.validarId(idPedido)) throw new PedidoIdInvalidoException();

        if (!FuncoesValidacao.validarId(idEstabelecimento)) throw new EstabelecimentoIdInvalidoException();
    }

}
