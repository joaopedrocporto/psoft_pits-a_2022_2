package com.ufcg.psoft.commerce.service.EstabelecimentoService;

import com.ufcg.psoft.commerce.exception.ClienteException.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoExisteEntregadorDisponivelException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoNaoExisteException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoStatusInvalidoException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstabelecimentoNotificarPedidoNaoPodeSerAtribuidoPadraoService
        implements EstabelecimentoNotificarPedidoNaoPodeSerAtribuidoService {
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    ClienteRepository clienteRepository;

    public void notificarClientePedidoNaoPodeSerAtribuido(Long pedidoId)
    throws
            PedidoNaoExisteException,
            EstabelecimentoNaoExisteException,
            ClienteNaoExisteException,
            PedidoStatusInvalidoException,
            PedidoIdInvalidoException,
            EstabelecimentoExisteEntregadorDisponivelException
    {
        this.validarId(pedidoId);

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(PedidoNaoExisteException::new);
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(pedido.getEstabelecimentoId())
                .orElseThrow(EstabelecimentoNaoExisteException::new);
        Cliente cliente = clienteRepository.findById(pedido.getClienteId())
                .orElseThrow(ClienteNaoExisteException::new);

        this.validarStatusPedido(pedido.getStatusEntrega());
        this.verificarEntregadoresDisponiveis(estabelecimento);

        this.notificar(cliente.getNome(), pedido.getId());
    }

    private String notificar(String cliente, Long id) {
        String notificacao = String.format(
                "Olá, %s! Seu pedido #%d está pronto, mas não há entregadores disponíveis no momento.",
                cliente,
                id
        );
        return notificacao;
    }

    private void validarStatusPedido(String statusEntrega) {
        if (!statusEntrega.equals("Pedido pronto")) throw new PedidoStatusInvalidoException();
    }

    private void validarId(Long idPedido) {
        if (!FuncoesValidacao.validarId(idPedido)) throw new PedidoIdInvalidoException();
    }

    private void verificarEntregadoresDisponiveis(Estabelecimento estabelecimento) {
        if (estabelecimento.verificarEntregadores()) {
            throw new EstabelecimentoExisteEntregadorDisponivelException();
        }
    }
}
