package com.ufcg.psoft.commerce.exception.AssociacaoException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;
import com.ufcg.psoft.commerce.model.Associacao;

public class AssociacaoIdInvalidoException extends PitsACommerceException {
    public AssociacaoIdInvalidoException() {super("Id de Associacao Invalido");}
}
