package com.ufcg.psoft.commerce.exception.SaborException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class SaborIndisponivelException extends PitsACommerceException {
    public SaborIndisponivelException() {super("O sabor procurado nao esta disponivel no momento!");}
}