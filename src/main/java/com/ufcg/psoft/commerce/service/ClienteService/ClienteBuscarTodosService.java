package com.ufcg.psoft.commerce.service.ClienteService;

import com.ufcg.psoft.commerce.model.Cliente;

import java.util.List;

@FunctionalInterface
public interface ClienteBuscarTodosService {
    public List<Cliente> buscarTodos();
}
