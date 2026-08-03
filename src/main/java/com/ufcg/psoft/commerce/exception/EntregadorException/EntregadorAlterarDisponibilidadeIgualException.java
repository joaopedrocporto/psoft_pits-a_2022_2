package com.ufcg.psoft.commerce.exception.EstabelecimentoException;

import com.ufcg.psoft.commerce.exception.PitsACommerceException;

public class EntregadorAlterarDisponibilidadeIgualException extends PitsACommerceException {
    public  EntregadorAlterarDisponibilidadeIgualException() {
        super("A disponibilidade não pode ser igual a anterior!");
    }
}
