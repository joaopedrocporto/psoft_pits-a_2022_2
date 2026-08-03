package com.ufcg.psoft.commerce.exception.SaborException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class SaborJaEstavaIndisponivelException extends PitsACommerceException {
    public SaborJaEstavaIndisponivelException() {super("O sabor consultado ja esta indisponivel!");}
}