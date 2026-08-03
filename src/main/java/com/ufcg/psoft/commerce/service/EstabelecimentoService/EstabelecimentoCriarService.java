package com.ufcg.psoft.commerce.service.EstabelecimentoService;

import com.ufcg.psoft.commerce.dto.EstabelecimentoDTO.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Estabelecimento;

@FunctionalInterface
public interface EstabelecimentoCriarService {
    Estabelecimento criar(EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO);
}