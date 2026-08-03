package com.ufcg.psoft.commerce.service.AssociacaoService;

import com.ufcg.psoft.commerce.model.Associacao;

import java.util.List;

@FunctionalInterface
public interface AssociacaoExibirTodosService {
    public List<Associacao> exibirTodos(
            Long entrgadorId,
            String codigoAcesso
    );
}