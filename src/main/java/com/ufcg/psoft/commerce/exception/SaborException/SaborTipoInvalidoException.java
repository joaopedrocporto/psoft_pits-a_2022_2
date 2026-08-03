package com.ufcg.psoft.commerce.exception.SaborException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class SaborTipoInvalidoException extends PitsACommerceException {
    public SaborTipoInvalidoException() {super("Tipo deve ser salgado ou doce");}
}