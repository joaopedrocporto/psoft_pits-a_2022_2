package com.ufcg.psoft.commerce.exception.PedidoException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class PedidoNaoExisteException extends PitsACommerceException {
    public PedidoNaoExisteException() {
        super("O pedido consultado nao existe!");
    }
}
