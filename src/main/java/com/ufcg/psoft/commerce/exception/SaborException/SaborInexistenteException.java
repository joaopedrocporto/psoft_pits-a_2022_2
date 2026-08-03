package com.ufcg.psoft.commerce.exception.SaborException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class SaborInexistenteException extends PitsACommerceException {
    public SaborInexistenteException() {super("O sabor consultado nao existe!");}
}