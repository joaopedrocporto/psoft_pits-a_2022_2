package com.ufcg.psoft.commerce.service.EntregadorService;

import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorResponseDTO;

@FunctionalInterface
public interface EntregadorAlterarService {
    EntregadorResponseDTO alterarEntregador(
            Long id,
            String codigoAcesso,
            EntregadorPostPutRequestDTO entregadorPostPutRequestDTO
    );
}
