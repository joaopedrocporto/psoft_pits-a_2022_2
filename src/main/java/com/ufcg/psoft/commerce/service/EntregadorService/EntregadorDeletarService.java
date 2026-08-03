package com.ufcg.psoft.commerce.service.EntregadorService;

@FunctionalInterface
public interface EntregadorDeletarService {
    void deletarEntregador(Long id, String codigoAcesso);
}
