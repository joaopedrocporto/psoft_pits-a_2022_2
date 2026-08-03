package com.ufcg.psoft.commerce.exception.SaborException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class SaborDisponivelException extends PitsACommerceException {
    public SaborDisponivelException() {super("O sabor consultado ja esta disponivel!");}
}
