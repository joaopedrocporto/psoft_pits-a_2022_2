package com.ufcg.psoft.commerce.service.AssociacaoService;

import com.ufcg.psoft.commerce.model.Associacao;

@FunctionalInterface
public interface AssociacaoExibirService {
    public Associacao exbir(
            Long entregadorId,
            String codigoAcessoEntregador,
            Long estabelecimentoId
    );
}
