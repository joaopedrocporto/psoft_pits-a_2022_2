package com.ufcg.psoft.commerce.service.EntregadorService;

import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorResponseDTO;

import java.util.List;

@FunctionalInterface
public interface EntregadorListarService {
    List<EntregadorResponseDTO> listar();
}
