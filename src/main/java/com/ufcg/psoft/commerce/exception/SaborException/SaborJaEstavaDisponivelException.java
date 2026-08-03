package com.ufcg.psoft.commerce.exception.SaborException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class SaborJaEstavaDisponivelException extends PitsACommerceException {
    public SaborJaEstavaDisponivelException() {super("O sabor consultado ja esta disponivel!");}
}