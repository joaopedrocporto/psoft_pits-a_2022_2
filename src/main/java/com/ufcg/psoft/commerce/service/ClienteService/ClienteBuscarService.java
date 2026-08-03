package com.ufcg.psoft.commerce.service.ClienteService;

import com.ufcg.psoft.commerce.dto.ClienteDTO.ClienteResponseDTO;

@FunctionalInterface
public interface ClienteBuscarService {

    public ClienteResponseDTO buscar(Long id);
}