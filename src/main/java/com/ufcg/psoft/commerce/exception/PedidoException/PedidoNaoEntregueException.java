package com.ufcg.psoft.commerce.exception.PedidoException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class PedidoNaoEntregueException extends PitsACommerceException {
    public PedidoNaoEntregueException() {
        super("O pedido não foi entregue ainda!");
    }
}
