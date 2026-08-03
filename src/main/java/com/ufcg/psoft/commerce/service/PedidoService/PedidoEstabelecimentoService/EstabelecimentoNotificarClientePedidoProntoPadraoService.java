package com.ufcg.psoft.commerce.service.PedidoService.PedidoEstabelecimentoService;

import com.ufcg.psoft.commerce.exception.ClienteException.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorNaoExisteException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoNaoExisteException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoStatusInvalidoException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstabelecimentoNotificarClientePedidoProntoPadraoService implements EstabelecimentoNotificarClientePedidoProntoService{
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    EntregadorRepository entregadorRepository;

    public String notificarClientePedidoPronto(Long pedidoId)
    throws
            PedidoNaoExisteException,
            ClienteNaoExisteException,
            EntregadorNaoExisteException,
            PedidoStatusInvalidoException,
            PedidoIdInvalidoException
    {
        this.validarId(pedidoId);

        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);

        this.verificarStatusPedido(pedido.getStatusEntrega());

        Cliente cliente = clienteRepository.findById(pedido.getClienteId()).orElseThrow(ClienteNaoExisteException::new);
        Entregador entregador = entregadorRepository.findById(pedido.getEntregadorId())
                                .orElseThrow(EntregadorNaoExisteException::new);

        String notificacao = "Olá, " + cliente.getNome() + "! O seu pedido #" + pedido.getId() +
                ", está pronto e em rota de entrega!\n" +
                "O entregador " + entregador.getNome() + " já está em rota de entrega!\n" +
                "Tipo do Veículo: " + entregador.getTipoVeiculo() + "\n" +
                "Placa do Veículo: " + entregador.getPlacaVeiculo() + "\n" +
                "Cor do Veículo: " + entregador.getCorVeiculo();

        return notificacao;
    }

    private void validarId(Long idPedido) {
        if (!FuncoesValidacao.validarId(idPedido)) throw new PedidoIdInvalidoException();
    }

    private void verificarStatusPedido(String status) {
        if (!status.toUpperCase().equals("PEDIDO PRONTO")) {
            throw new PedidoStatusInvalidoException();
        }
    }

}
