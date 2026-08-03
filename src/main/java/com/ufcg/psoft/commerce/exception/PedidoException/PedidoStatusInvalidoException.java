package com.ufcg.psoft.commerce.exception.PedidoException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class PedidoStatusInvalidoException extends PitsACommerceException {
    public PedidoStatusInvalidoException() {super("O status do pedido e invalido!");}
}
