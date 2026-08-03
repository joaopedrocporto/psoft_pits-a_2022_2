package com.ufcg.psoft.commerce.service.SaborService;

import com.ufcg.psoft.commerce.model.Sabor;

@FunctionalInterface
public interface EditarSaborDisponibilidadeService {
    Sabor editarDisponibilidade(Long saborId, Long estabelecimentoId, String estabelecimentoCodigoAcesso, Boolean disponibilidade);
}