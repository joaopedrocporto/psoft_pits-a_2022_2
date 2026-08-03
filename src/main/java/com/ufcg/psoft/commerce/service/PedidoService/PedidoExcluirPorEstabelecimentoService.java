package com.ufcg.psoft.commerce.service.PedidoService;

@FunctionalInterface
public interface PedidoExcluirPorEstabelecimentoService {
    void excluir(Long pedidoId, Long estabelecimentoExclusorId, String codigoDeAcesso);
}
