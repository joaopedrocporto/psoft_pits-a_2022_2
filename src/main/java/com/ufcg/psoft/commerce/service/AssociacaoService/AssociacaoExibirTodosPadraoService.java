package com.ufcg.psoft.commerce.service.AssociacaoService;

import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorGetRequestDTO;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorNaoExisteException;
import com.ufcg.psoft.commerce.model.Associacao;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.repository.AssociacaoRepository;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssociacaoExibirTodosPadraoService implements AssociacaoExibirTodosService {
    @Autowired
    AssociacaoRepository associacaoRepository;
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<Associacao> exibirTodos(Long entrgadorId, String codigoAcesso)
            throws EntregadorIdInvalidoException, EntregadorNaoExisteException, CodigoDeAcessoInvalidoException
    {
        validarId(entrgadorId);

        Entregador entregador = this.entregadorRepository
                .findById(entrgadorId)
                .orElseThrow(EntregadorNaoExisteException::new);

        validarCodigoAcesso(entregador.getCodigoAcesso(), codigoAcesso);
        return this.associacaoRepository.retornarAssociacoes(entrgadorId);
    }

    private void validarId(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new EntregadorIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
    }
}
