package com.ufcg.psoft.commerce.exception.AssociacaoException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class AssociacaoJaExisteException extends PitsACommerceException {
    public AssociacaoJaExisteException() {super("Essa associacao ja existe");}
}
