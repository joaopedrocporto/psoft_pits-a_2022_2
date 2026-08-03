package com.ufcg.psoft.commerce.exception.ClienteException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class ClienteNaoExisteException extends PitsACommerceException {
    public ClienteNaoExisteException() {super("O cliente consultado nao existe!");}
}