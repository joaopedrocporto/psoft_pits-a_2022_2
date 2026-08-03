package com.ufcg.psoft.commerce.service.PedidoService.PedidoClienteService;

@FunctionalInterface
public interface ClienteExcluirPedidoService {
    void excluir(Long pedidoId, Long clienteId, String codigoAcessoCliente);
}
