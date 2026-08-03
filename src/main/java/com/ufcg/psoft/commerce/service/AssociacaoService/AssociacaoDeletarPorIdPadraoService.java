package com.ufcg.psoft.commerce.service.AssociacaoService;

import com.ufcg.psoft.commerce.exception.AssociacaoException.AssociacaoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.AssociacaoException.AssociacaoJaExisteException;
import com.ufcg.psoft.commerce.exception.AssociacaoException.AssociacaoNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorNaoExisteException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.model.Associacao;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.repository.AssociacaoRepository;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AssociacaoDeletarPorIdPadraoService implements AssociacaoDeletarPorIdService {
    @Autowired
    AssociacaoRepository associacaoRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    EntregadorRepository entregadorRepository;

    @Override
    public void deletarPorID(Long idAssociacao, String codigoAcesso)
    throws AssociacaoNaoExisteException, AssociacaoIdInvalidoException, CodigoDeAcessoInvalidoException
    {
        validarId(idAssociacao);

        Associacao associacao = this.associacaoRepository
                .findById(idAssociacao)
                .orElseThrow(AssociacaoNaoExisteException::new);
        validarCodigoAcesso(associacao.getCodigoAcesso(), codigoAcesso);

        Entregador entregador = this.entregadorRepository
                .findById(associacao.getEntregadorId())
                .orElseThrow(EntregadorNaoExisteException::new);

        Estabelecimento estabelecimento = this.estabelecimentoRepository
                .findById(associacao.getEstabelecimentoId())
                .orElseThrow(EstabelecimentoNaoEncontradoException::new);

        atualizarListaEntregadores(estabelecimento, entregador);
        this.associacaoRepository.deleteById(idAssociacao);
    }

    private void atualizarListaEntregadores (Estabelecimento estabelecimento, Entregador entregador) {
        estabelecimento.removerEntregador(entregador);
        this.estabelecimentoRepository.save(estabelecimento);
    }

    private void validarId(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new AssociacaoIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
    }
}
