package com.ufcg.psoft.commerce.service.EntregadorService;

import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorResponseDTO;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EntregadorListarPadraoService implements EntregadorListarService{
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<EntregadorResponseDTO> listar() {
        List<EntregadorResponseDTO> entregadoresResponseDTOS = new ArrayList<>();
        for (Entregador entregador: this.entregadorRepository.findAll()) {
            EntregadorResponseDTO entregadorResponseDTO = modelMapper.map(entregador, EntregadorResponseDTO.class);
            entregadoresResponseDTOS.add(entregadorResponseDTO);
        }
        return entregadoresResponseDTOS;
    }
}
