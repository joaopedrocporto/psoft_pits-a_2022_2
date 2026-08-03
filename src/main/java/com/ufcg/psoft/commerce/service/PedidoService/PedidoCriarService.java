package com.ufcg.psoft.commerce.service.PedidoService;

import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Pedido;

@FunctionalInterface
public interface PedidoCriarService {

    Pedido criar(Long clienteId, String clienteCodigoAcesso, Long estabelecimentoId, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);
}
