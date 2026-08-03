package com.ufcg.psoft.commerce.service.EntregadorService;

import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorResponseDTO;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorNaoExisteException;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EntregadorBuscarPadraoService implements EntregadorBuscarService{
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    EntregadorRepository entregadorRepository;

    @Override
    public EntregadorResponseDTO buscar(Long id) throws EntregadorNaoExisteException, EntregadorIdInvalidoException {
        validarID(id);
        Entregador entregador = this.entregadorRepository.findById(id)
                .orElseThrow(EntregadorNaoExisteException::new);
        return modelMapper.map(entregador, EntregadorResponseDTO.class);
    }

    private void validarID(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new EntregadorIdInvalidoException();
    }
}
