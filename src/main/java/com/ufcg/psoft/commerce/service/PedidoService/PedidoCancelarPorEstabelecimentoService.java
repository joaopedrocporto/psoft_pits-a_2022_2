package com.ufcg.psoft.commerce.service.PedidoService;

@FunctionalInterface
public interface PedidoCancelarPorEstabelecimentoService {
    void cancelar(Long estabelecimentoId, Long pedidoId);
}
