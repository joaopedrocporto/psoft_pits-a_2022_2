package com.ufcg.psoft.commerce.service.EstabelecimentoService;

import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class EstabelecimentoListarPadraoService implements EstabelecimentoListarService {
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    public List<Estabelecimento> get(Long id) throws EstabelecimentoIdInvalidoException {
        if (FuncoesValidacao.isNull(id)) {
            return this.estabelecimentoRepository.findAll();
        } else {
            if (!FuncoesValidacao.validarId(id)) throw new EstabelecimentoIdInvalidoException();

            Estabelecimento estabelecimento = this.estabelecimentoRepository.findById(id).orElseThrow(EstabelecimentoNaoEncontradoException::new);

            return Arrays.asList(estabelecimento);
        }
    }
}