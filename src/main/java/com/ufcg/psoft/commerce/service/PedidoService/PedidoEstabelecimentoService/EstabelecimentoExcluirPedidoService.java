package com.ufcg.psoft.commerce.service.PedidoService.PedidoEstabelecimentoService;

@FunctionalInterface
public interface EstabelecimentoExcluirPedidoService {

    void excluir(Long pedidoId, Long estabelecimentoId, String codigoAcesso);
}
