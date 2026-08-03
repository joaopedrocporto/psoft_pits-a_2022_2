package com.ufcg.psoft.commerce.service.SaborService;

import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborIdInvalidoException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborInexistenteException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborJaEstavaDisponivelException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborJaEstavaIndisponivelException;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.SaborRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditarSaborDisponibilidadePadraoService implements EditarSaborDisponibilidadeService {
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    SaborRepository saborRepository;

    public Sabor editarDisponibilidade(
            Long saborId,
            Long estabelecimentoId,
            String estabelecimentoCodigoAcesso,
            Boolean disponibilidade
    ) throws
            SaborIdInvalidoException, EstabelecimentoIdInvalidoException, EstabelecimentoNaoEncontradoException,
            SaborInexistenteException, SaborJaEstavaDisponivelException, SaborJaEstavaIndisponivelException,
            CodigoDeAcessoInvalidoException
    {
        validarIds(saborId, estabelecimentoId);
        Estabelecimento estabelecimento = estabelecimentoRepository
                .findById(estabelecimentoId)
                .orElseThrow(EstabelecimentoNaoEncontradoException::new);
        validarCodigoAcesso(estabelecimento.getCodigoAcesso(), estabelecimentoCodigoAcesso);

        Sabor sabor = saborRepository.findById(saborId).orElseThrow(SaborInexistenteException::new);

        this.verificarDisponibilidade(sabor, disponibilidade);

        sabor.setDisponivel(disponibilidade);

        return saborRepository.save(sabor);
    }

    private void verificarDisponibilidade(Sabor sabor, Boolean disponibilidade) {
        if (!sabor.isDisponivel() && !disponibilidade) throw new SaborJaEstavaIndisponivelException();
        if (sabor.isDisponivel() && disponibilidade) throw new SaborJaEstavaDisponivelException();
    }

    private void validarIds(Long idSabor, Long idEstabelecimento) {
        if (!FuncoesValidacao.validarId(idSabor)) throw new SaborIdInvalidoException();

        if (!FuncoesValidacao.validarId(idEstabelecimento)) throw new EstabelecimentoIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
    }
}