package com.ufcg.psoft.commerce.exception.EntregadorException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class EntregadorDisponibilidadeInvalidaException extends PitsACommerceException {
    public EntregadorDisponibilidadeInvalidaException() { super("Disponibilidade deve ser true ou false");}
}
