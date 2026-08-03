package com.ufcg.psoft.commerce.exception.CodigoAcessoException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class CodigoDeAcessoInvalidoException extends PitsACommerceException {
    public CodigoDeAcessoInvalidoException() {
        super("Codigo de acesso invalido!");
    }
}