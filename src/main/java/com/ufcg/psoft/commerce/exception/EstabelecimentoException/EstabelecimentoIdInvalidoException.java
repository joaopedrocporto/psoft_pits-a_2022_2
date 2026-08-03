package com.ufcg.psoft.commerce.exception.EstabelecimentoException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class EstabelecimentoIdInvalidoException extends PitsACommerceException {
    public EstabelecimentoIdInvalidoException() {
        super("Estabelecimento Id Invalido");
    }
}