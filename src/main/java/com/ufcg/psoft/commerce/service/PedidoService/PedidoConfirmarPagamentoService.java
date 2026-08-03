package com.ufcg.psoft.commerce.service.PedidoService;

import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Pedido;
import org.springframework.stereotype.Service;

@FunctionalInterface
public interface PedidoConfirmarPagamentoService {
    Pedido confirmarPagamento(Long pedidoId, String metodoPagamento, String clienteCodigoAcesso);
}
