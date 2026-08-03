package com.ufcg.psoft.commerce.service.ClienteService;

import com.ufcg.psoft.commerce.dto.ClienteDTO.ClientePostPutRequestDTO;
import com.ufcg.psoft.commerce.exception.ClienteException.ClienteJaExisteException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ClienteCriarPadraoService implements ClienteCriarService{

    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Cliente criar(ClientePostPutRequestDTO novoClienteDTO) {
        Cliente novoCliente = modelMapper.map(novoClienteDTO,Cliente.class);
        return this.clienteRepository.save(novoCliente);
    }
}