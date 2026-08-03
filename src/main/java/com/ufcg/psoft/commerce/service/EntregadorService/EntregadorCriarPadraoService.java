package com.ufcg.psoft.commerce.service.EntregadorService;

import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EntregadorCriarPadraoService implements EntregadorCriarService{
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Entregador salvar(EntregadorPostPutRequestDTO entregadorPostPutRequestDTO) {
        Entregador entregador = this.modelMapper.map(entregadorPostPutRequestDTO, Entregador.class);
        return this.entregadorRepository.save(entregador);
    }
}
