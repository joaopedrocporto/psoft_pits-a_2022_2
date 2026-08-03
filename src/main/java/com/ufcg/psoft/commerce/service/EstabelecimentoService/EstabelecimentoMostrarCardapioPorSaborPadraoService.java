package com.ufcg.psoft.commerce.service.EstabelecimentoService;

import com.ufcg.psoft.commerce.dto.SaborDTO.SaborResponseDTO;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class EstabelecimentoMostrarCardapioPorSaborPadraoService implements EstabelecimentoMostrarCardapioPorSaborService{

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    public List<SaborResponseDTO> mostrar(Long id, String tipo){
        validarId(id);
        Estabelecimento estabelecimento = this.estabelecimentoRepository.findById(id).orElseThrow(EstabelecimentoNaoEncontradoException::new);

        List<SaborResponseDTO> resposta = new ArrayList<>();
        List<SaborResponseDTO> saboresDisponivel = new ArrayList<>();
        List<SaborResponseDTO> saboresIndisponivel = new ArrayList<>();
        Sabor[] sabores = estabelecimento.getSabores().toArray(new Sabor[estabelecimento.getSabores().size()]);
        for(int a = 0; a < sabores.length; a++){
            if(sabores[a].getTipo().equals(tipo)){
                SaborResponseDTO responseDTO = mapSaborToSaborResponseDTO(sabores[a]);
                resposta.add(responseDTO);
            }
        }
        for(SaborResponseDTO sabor : resposta){
            if(sabor.isDisponivel()){
                saboresDisponivel.add(sabor);
            }else{
                saboresIndisponivel.add(sabor);
            }
        }
        resposta.clear();
        resposta.addAll(saboresDisponivel);
        resposta.addAll(saboresIndisponivel);
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
