package com.ufcg.psoft.commerce.exception.EntregadorException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class EntregadorDisponibilidadeRepetidaException extends PitsACommerceException {
    public EntregadorDisponibilidadeRepetidaException(){super("A disponibilidade já era essa!");}
}
