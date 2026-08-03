package com.ufcg.psoft.commerce.service.EstabelecimentoService;

import com.ufcg.psoft.commerce.dto.EstabelecimentoDTO.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.*;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EstabelecimentoAlterarCodigoAcessoPadraoService implements EstabelecimentoAlterarCodigoAcessoService {
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    public Estabelecimento atualizarCodigo(
            Long id, String codigoAcesso,
            EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO) throws
            EstabelecimentoNaoEncontradoException, CodigoDeAcessoInvalidoException, EstabelecimentoIdInvalidoException
    {
        validarId(id);

        Estabelecimento estabelecimento = estabelecimentoRepository
                .findById(id)
                .orElseThrow(EstabelecimentoNaoEncontradoException::new);

        validarCodigoAcesso(estabelecimento.getCodigoAcesso(), codigoAcesso);
        estabelecimento.setCodigoAcesso(estabelecimentoPostPutRequestDTO.getCodigoAcesso());
        return this.estabelecimentoRepository.save(estabelecimento);
    }

    private void validarId(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new EstabelecimentoIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
    }
}