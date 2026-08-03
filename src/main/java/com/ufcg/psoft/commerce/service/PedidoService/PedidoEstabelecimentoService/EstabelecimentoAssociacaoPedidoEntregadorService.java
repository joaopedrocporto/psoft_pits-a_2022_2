package com.ufcg.psoft.commerce.service.PedidoService.PedidoEstabelecimentoService;

import com.ufcg.psoft.commerce.model.Pedido;

@FunctionalInterface
public interface EstabelecimentoAssociacaoPedidoEntregadorService {
    public Pedido associarPedidoEntregador(Long idPedido, Long idEstabelecimeneto, String codigoAcesso);
}