package com.ufcg.psoft.commerce.exception.SaborException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class MuitosSaboresException extends PitsACommerceException {
    public MuitosSaboresException() {
        super("A pizza media so pode ter um sabor");
    }
}
