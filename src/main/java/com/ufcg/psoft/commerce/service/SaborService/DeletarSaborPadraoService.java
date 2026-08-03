package com.ufcg.psoft.commerce.service.SaborService;


import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborIdInvalidoException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborInexistenteException;
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

@Service
@Transactional
public class DeletarSaborPadraoService implements DeletarSaborService{
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    SaborRepository saborRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Override
    public void deletar(
            Long idSabor,
            Long idEstabelecimento,
            String codigoAcessoEstabelecimento
    ) throws
            EstabelecimentoIdInvalidoException,
            SaborIdInvalidoException,
            SaborInexistenteException,
            EstabelecimentoNaoEncontradoException,
            CodigoDeAcessoInvalidoException
    {
        validarIds(idSabor, idEstabelecimento);
        Estabelecimento estabelimentoSabor = this.estabelecimentoRepository.findById(idEstabelecimento).orElseThrow(EstabelecimentoIdInvalidoException::new);
        validarCodigoAcesso(estabelimentoSabor.getCodigoAcesso(), codigoAcessoEstabelecimento);
        Sabor saborRemove = this.saborRepository.findById(idSabor).orElseThrow(SaborInexistenteException::new);
        if (!estabelimentoSabor.getSabores().contains(saborRemove)) throw new SaborInexistenteException();
        estabelimentoSabor.getSabores().remove(saborRemove);
        this.estabelecimentoRepository.save(estabelimentoSabor);
        this.saborRepository.delete(saborRemove);
    }

    private void validarIds(Long idSabor, Long idEstabelecimento) {
        if (!FuncoesValidacao.validarId(idSabor)) throw new SaborIdInvalidoException();

        if (!FuncoesValidacao.validarId(idEstabelecimento)) throw new EstabelecimentoIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();

    }
}