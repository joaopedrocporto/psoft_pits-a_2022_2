package com.ufcg.psoft.commerce.service.SaborService;

import com.ufcg.psoft.commerce.dto.SaborDTO.SaborPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Sabor;

@FunctionalInterface
public interface EditarSaborService {
    Sabor editar(SaborPostPutRequestDTO saborPostPutRequestDTO, Long idSabor, Long idEstabelecimento, String codigoAcessoEstabelecimento);
}