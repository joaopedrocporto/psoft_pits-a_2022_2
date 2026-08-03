package com.ufcg.psoft.commerce.service.SaborService;

import com.ufcg.psoft.commerce.dto.SaborDTO.SaborPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Sabor;

@FunctionalInterface
public interface CriarSaborService {
    Sabor criar(SaborPostPutRequestDTO saborPostPutRequestDTO, Long idEstabelecimento, String codigoAcessoEstabelecimento);
}