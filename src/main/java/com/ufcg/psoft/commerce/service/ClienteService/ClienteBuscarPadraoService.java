package com.ufcg.psoft.commerce.service.ClienteService;

import com.ufcg.psoft.commerce.dto.ClienteDTO.ClienteResponseDTO;
import com.ufcg.psoft.commerce.exception.ClienteException.ClienteIdInvalidoException;
import com.ufcg.psoft.commerce.exception.ClienteException.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ClienteBuscarPadraoService implements ClienteBuscarService {

    @Autowired
    ClienteRepository clienteRepository;

    @Override
    public ClienteResponseDTO buscar(Long idB) throws ClienteIdInvalidoException, ClienteNaoExisteException {
        validarId(idB);
        Cliente clienteEncontrado =  this.clienteRepository.findById(idB).orElseThrow(ClienteNaoExisteException::new);

        return ClienteResponseDTO.builder()
                .id(clienteEncontrado.getId()) // Defina o ID conforme necessário
                .nome(clienteEncontrado.getNome())
                .endereco(clienteEncontrado.getEndereco())
                .build();
    }


    private void validarId(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new ClienteIdInvalidoException();
    }
}