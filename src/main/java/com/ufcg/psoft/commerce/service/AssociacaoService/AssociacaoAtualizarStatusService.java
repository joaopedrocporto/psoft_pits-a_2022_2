package com.ufcg.psoft.commerce.service.AssociacaoService;

import com.ufcg.psoft.commerce.model.Associacao;

@FunctionalInterface
public interface AssociacaoAtualizarStatusService {
    public Associacao atualizarStatus(
            Long entregadorId,
            Long estabelecimentoId,
            String codigoAcessoEstabelecimento
    );
}
