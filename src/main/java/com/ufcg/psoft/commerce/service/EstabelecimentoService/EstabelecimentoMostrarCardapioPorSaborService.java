package com.ufcg.psoft.commerce.service.EstabelecimentoService;


import com.ufcg.psoft.commerce.dto.SaborDTO.SaborResponseDTO;
import com.ufcg.psoft.commerce.model.Estabelecimento;

import java.util.List;

@FunctionalInterface
public interface EstabelecimentoMostrarCardapioPorSaborService {
    public List<SaborResponseDTO> mostrar(Long id, String tipo);

}
