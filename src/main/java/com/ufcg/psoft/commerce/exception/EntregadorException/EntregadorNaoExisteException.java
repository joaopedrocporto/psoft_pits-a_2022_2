package com.ufcg.psoft.commerce.exception.EntregadorException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class EntregadorNaoExisteException extends PitsACommerceException {
    public EntregadorNaoExisteException(){super("O entregador consultado nao existe!");}
}
