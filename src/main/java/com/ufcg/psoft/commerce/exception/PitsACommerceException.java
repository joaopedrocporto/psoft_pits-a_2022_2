package com.ufcg.psoft.commerce.exception;

public class PitsACommerceException extends RuntimeException {
    public PitsACommerceException() {
        super("Erro imprevisto na aplicação!");
    }

    public PitsACommerceException(String error) {
        super(error);
    }
}
