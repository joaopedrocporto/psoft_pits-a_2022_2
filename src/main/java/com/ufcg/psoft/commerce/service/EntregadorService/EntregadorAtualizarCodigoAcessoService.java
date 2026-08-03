package com.ufcg.psoft.commerce.service.EntregadorService;

import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorCodigoAcessoPatchRequestDTO;
import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorGetRequestDTO;
import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorPostPutRequestDTO;

@FunctionalInterface
public interface EntregadorAtualizarCodigoAcessoService {
    public EntregadorGetRequestDTO atualizarCodigoAcesso(
            Long id,
            String codigoAcesso,
            EntregadorCodigoAcessoPatchRequestDTO entregadorCodigoAcessoPatchRequestDTO
    );
}
