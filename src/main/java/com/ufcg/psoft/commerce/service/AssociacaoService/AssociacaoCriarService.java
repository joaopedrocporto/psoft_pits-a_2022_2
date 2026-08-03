package com.ufcg.psoft.commerce.service.AssociacaoService;

import com.ufcg.psoft.commerce.model.Associacao;

@FunctionalInterface
public interface AssociacaoCriarService {
    public Associacao criar(
            Long entregadorId,
            String codigoAcessoEntregador,
            Long estabelecimentoId
    );
}
