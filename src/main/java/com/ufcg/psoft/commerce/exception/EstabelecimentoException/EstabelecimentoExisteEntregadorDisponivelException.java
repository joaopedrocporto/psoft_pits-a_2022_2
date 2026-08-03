package com.ufcg.psoft.commerce.exception.EstabelecimentoException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class EstabelecimentoExisteEntregadorDisponivelException extends PitsACommerceException {
    public EstabelecimentoExisteEntregadorDisponivelException() {
        super("Existe ao menos um entregador diponivel no estabelecimento!");
    }
}