package com.ufcg.psoft.commerce.service.ClienteService;

import com.ufcg.psoft.commerce.exception.ClienteException.ClienteIdInvalidoException;

@FunctionalInterface
public interface ClienteDeletarService {
    public void deletar(Long Id,String codigoAcesso);
}
