package com.ufcg.psoft.commerce.service.SaborService;

import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborIdInvalidoException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborInexistenteException;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.SaborRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.springframework.stereotype.Service;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoIdInvalidoException;

import java.util.ArrayList;
import java.util.List;
@Service
public class ExibirSaborPadraoService implements ExibirSaborService{
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    SaborRepository saborRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Override
    public List<Sabor> listar(
            Long idSabor,
            Long idEstabelecimento,
            String codigoAcessoEstabelecimento
    ) throws
            EstabelecimentoNaoEncontradoException,
            SaborIdInvalidoException,
            SaborInexistenteException,
            EstabelecimentoIdInvalidoException,
            CodigoDeAcessoInvalidoException
    {
        validarIdEstabelecimento(idEstabelecimento);
        Estabelecimento estabelimentoSabor = this.estabelecimentoRepository
                .findById(idEstabelecimento).orElseThrow(EstabelecimentoNaoEncontradoException::new);
        validarCodigoAcesso(estabelimentoSabor.getCodigoAcesso(),codigoAcessoEstabelecimento);

        if (FuncoesValidacao.isNull(idSabor)) {
            return new ArrayList<>(estabelimentoSabor.getSabores());
        }

        validarIdSabor(idSabor);
        Sabor sabor = this.saborRepository.findById(idSabor).orElseThrow(SaborInexistenteException::new);
        if (!estabelimentoSabor.getSabores().contains(sabor)) throw new SaborInexistenteException();
        return List.of(sabor);
    }

    private void validarIdSabor(Long idSabor) {
        if (!FuncoesValidacao.validarId(idSabor)) throw new SaborIdInvalidoException();
    }

    private void validarIdEstabelecimento(Long idEstabelecimento) {
        if (!FuncoesValidacao.validarId(idEstabelecimento)) throw new EstabelecimentoIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
    }
}