package com.ufcg.psoft.commerce.exception.EstabelecimentoException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class EstabelecimentoNaoEncontradoException extends PitsACommerceException {
    public EstabelecimentoNaoEncontradoException() {
        super("O estabelecimento consultado nao existe!");
    }
}