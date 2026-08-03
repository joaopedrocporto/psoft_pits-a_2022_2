package com.ufcg.psoft.commerce.service.PedidoService.PedidoClienteService;

import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoGetRequestDTO;

import java.util.List;

@FunctionalInterface
public interface ClienteVisualizarHistoricoPedidosService {
    List<PedidoGetRequestDTO> visualizarHistorico(Long idCliente, String codigoAcessoCliente);
}
