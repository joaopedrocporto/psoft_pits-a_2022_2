package com.ufcg.psoft.commerce.service.AssociacaoService;

import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorGetRequestDTO;
import com.ufcg.psoft.commerce.exception.AssociacaoException.AssociacaoNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorIdInvalidoException;
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
public class AssociacaoExibirPadraoService implements AssociacaoExibirService {
    @Autowired
    AssociacaoRepository associacaoRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Associacao exbir(Long entregadorId, String codigoAcessoEntregador, Long estabelecimentoId)
            throws EntregadorIdInvalidoException, CodigoDeAcessoInvalidoException,
            EntregadorNaoExisteException, AssociacaoNaoExisteException,
            EstabelecimentoNaoEncontradoException, EstabelecimentoIdInvalidoException
    {
        validarIds(entregadorId, estabelecimentoId);
        validarCodigo(codigoAcessoEntregador);

        Entregador entregador = this.entregadorRepository
                .findById(entregadorId)
                .orElseThrow(EntregadorNaoExisteException::new);

        this.estabelecimentoRepository
                .findById(estabelecimentoId)
                .orElseThrow(EstabelecimentoNaoEncontradoException::new);

        Associacao associacao = this.associacaoRepository.retornarAssociacao(entregadorId, estabelecimentoId);
        validarAssociacao(associacao);
        validarCodigoAcesso(entregador.getCodigoAcesso(), codigoAcessoEntregador);

        return associacao;
    }

    private void validarCodigo(String codigoAcessoEntregador) {
        if (!UtilCodigoAcesso.validarTamanho(codigoAcessoEntregador)) throw new CodigoDeAcessoInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
    }

    private void validarAssociacao(Associacao associacao) {
        if (FuncoesValidacao.isNull(associacao)) throw new AssociacaoNaoExisteException();
    }

    private void validarIds(Long entregadorId, Long estabelecimentoId) {
        if (!FuncoesValidacao.validarId(entregadorId)) throw new EntregadorIdInvalidoException();

        if (!FuncoesValidacao.validarId(estabelecimentoId)) throw new EstabelecimentoIdInvalidoException();
    }
}
