package com.ufcg.psoft.commerce.service.AssociacaoService;

import com.ufcg.psoft.commerce.model.Associacao;

@FunctionalInterface
public interface AssociacaoAtualizarService {
    public Associacao atualizar(
        Long entregadorId,
        String codigoAcessoEstabelecimento,
        Long estabelecimentoId
    );
}
