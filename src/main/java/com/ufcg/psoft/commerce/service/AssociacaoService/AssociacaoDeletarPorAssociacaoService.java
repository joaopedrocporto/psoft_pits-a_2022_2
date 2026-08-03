package com.ufcg.psoft.commerce.service.AssociacaoService;

@FunctionalInterface
public interface AssociacaoDeletarPorAssociacaoService {
    public void deletar(
            Long entregadorId,
            String codigoAcessoEntregador,
            Long estabelecimentoId
    );
}
