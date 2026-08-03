package com.ufcg.psoft.commerce.exception.PedidoException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class PedidoCancelamentoInvalidoException extends PitsACommerceException {

    public PedidoCancelamentoInvalidoException() {
        super("O pedido nao pode ser cancelado");
    }
}
