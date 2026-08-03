package com.ufcg.psoft.commerce.service.PedidoService;

import com.ufcg.psoft.commerce.model.Pedido;

@FunctionalInterface
public interface PedidoConfirmadoPorClienteService {
    Pedido confirmar(Long pedidoId, Long clienteId, String clienteCodigoAcesso);
}
