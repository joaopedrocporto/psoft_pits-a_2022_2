package com.ufcg.psoft.commerce.exception.ClienteException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class ClienteIdsDiferentesException extends PitsACommerceException {
    public ClienteIdsDiferentesException(){super("Id de cliente diferente do Id do cliente do pedido");}
}
