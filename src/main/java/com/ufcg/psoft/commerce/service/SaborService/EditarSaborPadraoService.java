package com.ufcg.psoft.commerce.service.SaborService;

import com.ufcg.psoft.commerce.dto.SaborDTO.SaborPostPutRequestDTO;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborIdInvalidoException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborInexistenteException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborTipoInvalidoException;
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

@Service
public class EditarSaborPadraoService implements EditarSaborService{
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    SaborRepository saborRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Override
    public Sabor editar(
            SaborPostPutRequestDTO saborPostPutRequestDTO,
            Long idSabor,
            Long idEstabelecimento,
            String codigoAcessoEstabelecimento
    ) throws
            EstabelecimentoIdInvalidoException,
            SaborIdInvalidoException,
            EstabelecimentoNaoEncontradoException,
            SaborInexistenteException,
            CodigoDeAcessoInvalidoException
    {
        validarIds(idSabor, idEstabelecimento);
        Estabelecimento estabelimentoSabor = this.estabelecimentoRepository
                .findById(idEstabelecimento).orElseThrow(EstabelecimentoNaoEncontradoException::new);

        validarCodigoAcesso(estabelimentoSabor.getCodigoAcesso(),codigoAcessoEstabelecimento);

        Sabor saborExistente = saborRepository.findById(idSabor).orElseThrow(SaborInexistenteException::new);
        if (!estabelimentoSabor.getSabores().contains(saborExistente)) throw new SaborInexistenteException();

        validarTipo(saborPostPutRequestDTO.getTipo());

        // mapeia sobre a entidade carregada: mapear para uma nova cria um registro extra
        modelMapper.map(saborPostPutRequestDTO, saborExistente);
        return this.saborRepository.save(saborExistente);
    }

    private void validarTipo(String tipo) {
        if (!"salgado".equals(tipo) && !"doce".equals(tipo)) throw new SaborTipoInvalidoException();
    }

    private void validarIds(Long idSabor, Long idEstabelecimento) {
        if (!FuncoesValidacao.validarId(idSabor)) throw new SaborIdInvalidoException();

        if (!FuncoesValidacao.validarId(idEstabelecimento)) throw new EstabelecimentoIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
    }
}