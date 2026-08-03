package com.ufcg.psoft.commerce.service.ClienteService;

import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteBuscarTodosPadraoService implements ClienteBuscarTodosService{

    @Autowired
    ClienteRepository clienteRepository;
    @Override
    public List<Cliente> buscarTodos() {
        return this.clienteRepository.findAll();
    }
}
