package com.ufcg.psoft.commerce.service.PedidoService;

@FunctionalInterface
public interface PedidoCancelarPorClienteService {
    void cancelar(Long pedidoId, String clienteCodigoAcesso);
}
