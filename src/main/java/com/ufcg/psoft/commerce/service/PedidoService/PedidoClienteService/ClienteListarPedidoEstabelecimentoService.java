package com.ufcg.psoft.commerce.service.PedidoService.PedidoClienteService;

import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoGetRequestDTO;

import java.util.List;

@FunctionalInterface
public interface ClienteListarPedidoEstabelecimentoService {
    List<PedidoGetRequestDTO> listarPedidoEstabelecimento(Long pedidoId, Long clienteId, Long estabelecimentoId,
                                     String codigoAcesso, String status);
}
