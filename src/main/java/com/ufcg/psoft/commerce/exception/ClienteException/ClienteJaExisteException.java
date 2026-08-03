package com.ufcg.psoft.commerce.exception.ClienteException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class ClienteJaExisteException extends PitsACommerceException {
    public ClienteJaExisteException() {super("Cliente ja existe");}
}
