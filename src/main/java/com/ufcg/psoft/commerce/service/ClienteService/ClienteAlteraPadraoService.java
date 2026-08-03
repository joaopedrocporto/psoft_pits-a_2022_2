package com.ufcg.psoft.commerce.service.ClienteService;

import com.ufcg.psoft.commerce.dto.ClienteDTO.ClientePostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.ClienteDTO.ClienteResponseDTO;
import com.ufcg.psoft.commerce.exception.ClienteException.ClienteIdInvalidoException;
import com.ufcg.psoft.commerce.exception.ClienteException.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ClienteAlteraPadraoService implements ClienteAlteraService {

    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public ClienteResponseDTO alterar(Long id, ClientePostPutRequestDTO clienteAlteradoDTO, String codigoAcesso)
    throws ClienteNaoExisteException, ClienteIdInvalidoException, CodigoDeAcessoInvalidoException
    {
        validarId(id);
        Cliente clienteExistente = this.clienteRepository.findById(id).orElseThrow(ClienteNaoExisteException::new);
        validarCodigoAcesso(clienteExistente.getCodigoAcesso(), codigoAcesso);
        Cliente clienteAlterado = this.modelMapper.map(clienteAlteradoDTO,Cliente.class);
        clienteAlterado.setId(id);
        this.clienteRepository.save(clienteAlterado);
        return ClienteResponseDTO.builder()
                .id(clienteAlterado.getId()) // Defina o ID conforme necessário
                .nome(clienteAlterado.getNome())
                .endereco(clienteAlterado.getEndereco())
                .build();
    }

    private void validarId(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new ClienteIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
    }
}
