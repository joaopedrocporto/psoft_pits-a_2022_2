package com.ufcg.psoft.commerce.service.SaborService;

import com.ufcg.psoft.commerce.dto.SaborDTO.SaborPostPutRequestDTO;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.SaborRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoIdInvalidoException;

import java.util.Set;

@Service
@Transactional
public class CriarSaborPadraoService implements CriarSaborService{
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    SaborRepository saborRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Override
    public Sabor criar(
            SaborPostPutRequestDTO saborPostPutRequestDTO,
            Long idEstabelecimento,
            String codigoAcessoEstabelecimento
    ) throws
            EstabelecimentoIdInvalidoException,
            EstabelecimentoNaoEncontradoException,
            CodigoDeAcessoInvalidoException
    {
        validarId(idEstabelecimento);
        Estabelecimento estabelimentoSabor = this.estabelecimentoRepository
                .findById(idEstabelecimento)
                .orElseThrow(EstabelecimentoNaoEncontradoException::new);
        validarCodigoAcesso(estabelimentoSabor.getCodigoAcesso(),codigoAcessoEstabelecimento);
        Set<Sabor> sabores = estabelimentoSabor.getSabores();
        Sabor novoSabor = modelMapper.map(saborPostPutRequestDTO,Sabor.class);
        Sabor saborSalvo = this.saborRepository.save(novoSabor);
        sabores.add(saborSalvo);
        estabelimentoSabor.setSabores(sabores);
        this.estabelecimentoRepository.save(estabelimentoSabor);
        return saborSalvo;
    }

    private void validarId(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new EstabelecimentoIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
    }
}