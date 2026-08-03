package com.ufcg.psoft.commerce.service.AssociacaoService;

import com.ufcg.psoft.commerce.dto.AssociacaoDTO.AssociacaoPostPutRequestDTO;
import com.ufcg.psoft.commerce.exception.AssociacaoException.AssociacaoJaExisteException;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorIndisponivelException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorNaoExisteException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.model.Associacao;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.repository.AssociacaoRepository;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssociacaoCriarPadraoService implements AssociacaoCriarService {
    @Autowired
    AssociacaoRepository associacaoRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Associacao criar(Long entregadorId, String codigoAcessoEntregador, Long estabelecimentoId)
        throws EntregadorIdInvalidoException, EstabelecimentoIdInvalidoException, EstabelecimentoNaoEncontradoException,
            EntregadorNaoExisteException, CodigoDeAcessoInvalidoException, AssociacaoJaExisteException
    {
        validarIds(entregadorId, estabelecimentoId);

        Entregador entregador = this.entregadorRepository
                .findById(entregadorId)
                .orElseThrow(EntregadorNaoExisteException::new);

        this.estabelecimentoRepository
                .findById(estabelecimentoId)
                .orElseThrow(EstabelecimentoNaoEncontradoException::new);

        validarCodigoAcesso(entregador.getCodigoAcesso(), codigoAcessoEntregador);
        validarDisponibilidade(entregador);

        Associacao associacaoDoRep = this.associacaoRepository.retornarAssociacao(entregadorId, estabelecimentoId);
        validarAssociacao(associacaoDoRep);

        AssociacaoPostPutRequestDTO associacaoPostPutRequestDTO = AssociacaoPostPutRequestDTO.builder()
                .codigoAcesso(codigoAcessoEntregador)
                .build();

        Associacao associacao = this.modelMapper.map(associacaoPostPutRequestDTO, Associacao.class);
        associacao.setEntregadorId(entregadorId);
        associacao.setEstabelecimentoId(estabelecimentoId);


        return this.associacaoRepository.save(associacao);
    }

    private void validarAssociacao(Associacao associacao) {
        if (FuncoesValidacao.isNotNull(associacao)) throw new AssociacaoJaExisteException();
    }

    private void validarDisponibilidade(Entregador entregador) {
        if (!entregador.getDisponibilidade()) throw new EntregadorIndisponivelException();
    }

    private void validarIds(Long entregadorId, Long estabelecimentoId) {
        if (!FuncoesValidacao.validarId(entregadorId)) throw new EntregadorIdInvalidoException();

        if (!FuncoesValidacao.validarId(estabelecimentoId)) throw new EstabelecimentoIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
    }
}
