package com.ufcg.psoft.commerce.service.PedidoService;

import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Pedido;

@FunctionalInterface
public interface PedidoEditarService {

    Pedido editar(Long pedidoId, String clienteCodigoAcesso, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);
}
