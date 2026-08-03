package com.ufcg.psoft.commerce.service.ClienteService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.ufcg.psoft.commerce.dto.ClienteDTO.ClientePostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
@FunctionalInterface
public interface ClienteCriarService{

    public Cliente criar(ClientePostPutRequestDTO novoCliente);

}
