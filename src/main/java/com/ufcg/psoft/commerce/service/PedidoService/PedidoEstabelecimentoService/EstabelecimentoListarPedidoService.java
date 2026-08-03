package com.ufcg.psoft.commerce.service.PedidoService.PedidoEstabelecimentoService;

import com.ufcg.psoft.commerce.model.Pedido;

import java.util.List;

@FunctionalInterface
public interface EstabelecimentoListarPedidoService {
    List<Pedido> listar(Long pedidoId, Long estabelecimentoId, String codigoAcesso);
}
