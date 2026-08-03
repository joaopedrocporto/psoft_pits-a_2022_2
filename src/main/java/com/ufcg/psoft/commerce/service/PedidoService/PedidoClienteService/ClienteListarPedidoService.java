package com.ufcg.psoft.commerce.service.PedidoService.PedidoClienteService;

import com.ufcg.psoft.commerce.model.Pedido;

import java.util.List;

@FunctionalInterface
public interface ClienteListarPedidoService {
    List<Pedido> listar(Long pedidoId, Long clienteId, String codigoAcesso);
}
