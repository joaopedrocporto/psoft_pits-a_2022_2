package com.ufcg.psoft.commerce.service.AssociacaoService;

@FunctionalInterface
public interface AssociacaoDeletarPorIdService {
    public void deletarPorID(Long idAssociacao, String codigoAcesso);
}
