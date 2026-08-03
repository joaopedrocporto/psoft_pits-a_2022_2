package com.ufcg.psoft.commerce.exception.EstabelecimentoException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class EstabelecimentoNaoExisteException extends PitsACommerceException {
    public  EstabelecimentoNaoExisteException() {
        super("O estabelecimento consultado nao existe!");
    }
}
