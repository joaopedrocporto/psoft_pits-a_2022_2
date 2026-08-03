package com.ufcg.psoft.commerce.service.EstabelecimentoService;


import com.ufcg.psoft.commerce.dto.SaborDTO.SaborResponseDTO;

import java.util.List;

@FunctionalInterface
public interface EstabelecimentoMostrarOCardapioService {
    public List<SaborResponseDTO> mostrar(Long idEstabelecimento);

}
