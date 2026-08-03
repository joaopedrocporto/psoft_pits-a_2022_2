package com.ufcg.psoft.commerce.exception.PedidoException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class PedidoConsultadoNaoExisteException extends PitsACommerceException {
    public PedidoConsultadoNaoExisteException() {
        super("O pedido consultado nao existe!");
    }
}