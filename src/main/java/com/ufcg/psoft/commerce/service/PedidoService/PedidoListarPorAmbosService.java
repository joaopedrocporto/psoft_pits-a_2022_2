package com.ufcg.psoft.commerce.service.PedidoService;

import com.ufcg.psoft.commerce.model.Pedido;

import java.util.List;

@FunctionalInterface
public interface PedidoListarPorAmbosService {
    List<Pedido> listar(Long clienteId, Long estabelecimentoId, Long pedidoId);
}
