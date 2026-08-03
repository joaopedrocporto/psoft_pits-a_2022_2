package com.ufcg.psoft.commerce.service.EntregadorService;

import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Entregador;

@FunctionalInterface
public interface EntregadorCriarService {
    Entregador salvar(EntregadorPostPutRequestDTO entregadorPostPutRequestDTO);
}
