package com.ufcg.psoft.commerce.service.EstabelecimentoService;

import com.ufcg.psoft.commerce.dto.SaborDTO.SaborResponseDTO;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstabelecimentoMostrarOCardapioPadraoService implements EstabelecimentoMostrarOCardapioService {

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    public List<SaborResponseDTO> mostrar(Long id){
        validarId(id);
        Estabelecimento estabelecimento = this.estabelecimentoRepository.findById(id).orElseThrow(EstabelecimentoNaoEncontradoException::new);

        List<SaborResponseDTO> resposta = new ArrayList<>();
        List<Sabor> sabores = new ArrayList<>(estabelecimento.getSabores());

        for (Sabor sabor : sabores) {
            SaborResponseDTO responseDTO = mapSaborToSaborResponseDTO(sabor);
            resposta.add(responseDTO);
        }
        return resposta;
    }

    public SaborResponseDTO mapSaborToSaborResponseDTO(Sabor sabor) {
        SaborResponseDTO responseDTO = new SaborResponseDTO();
        responseDTO.setNome(sabor.getNome());
        responseDTO.setPrecoM(sabor.getPrecoM());
        responseDTO.setPrecoG(sabor.getPrecoG());
        responseDTO.setDisponivel(sabor.isDisponivel());
        return responseDTO;
    }

    private void validarId(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new EstabelecimentoIdInvalidoException();
    }
}
