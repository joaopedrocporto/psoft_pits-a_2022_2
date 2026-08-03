package com.ufcg.psoft.commerce.service.PedidoService.PedidoClienteService;

import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoGetRequestDTO;

import java.util.List;

@FunctionalInterface
public interface ClienteFiltrarPedidosPorStatusService {

    List<PedidoGetRequestDTO> filtrarPorStatus(Long idCliente, String statusEntrega, String codigoAcessoCliente);
}
