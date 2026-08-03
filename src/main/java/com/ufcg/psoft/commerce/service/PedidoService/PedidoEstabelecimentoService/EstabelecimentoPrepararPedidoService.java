package com.ufcg.psoft.commerce.service.PedidoService.PedidoEstabelecimentoService;

import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Pedido;

@FunctionalInterface
public interface EstabelecimentoPrepararPedidoService {
    public Pedido prepararPedido(
            Long idPedido,
            Long idEstabelecimento,
            String codigoAcesso
                               );
}
