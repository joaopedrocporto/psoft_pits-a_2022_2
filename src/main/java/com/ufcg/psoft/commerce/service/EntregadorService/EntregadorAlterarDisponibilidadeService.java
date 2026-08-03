package com.ufcg.psoft.commerce.service.EntregadorService;

import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorResponseDTO;

@FunctionalInterface
public interface EntregadorAlterarDisponibilidadeService {
    EntregadorResponseDTO alterarDisponibilidade(Long id, String codigoAcesso, boolean disponibilidade);
}
