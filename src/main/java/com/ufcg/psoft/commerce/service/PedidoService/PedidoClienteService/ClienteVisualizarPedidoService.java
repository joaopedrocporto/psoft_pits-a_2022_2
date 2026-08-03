package com.ufcg.psoft.commerce.service.PedidoService.PedidoClienteService;

import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoGetRequestDTO;
@FunctionalInterface
public interface ClienteVisualizarPedidoService {

    public PedidoGetRequestDTO visualizar(Long clienteId, Long pedidoId, String codigoAcesso);
}
