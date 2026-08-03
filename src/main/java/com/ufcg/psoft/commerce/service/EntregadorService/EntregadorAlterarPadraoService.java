package com.ufcg.psoft.commerce.service.EntregadorService;

import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorResponseDTO;
import com.ufcg.psoft.commerce.exception.AssociacaoException.AssociacaoNaoExisteException;
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

import java.util.List;

@Service
public class EntregadorAlterarPadraoService implements EntregadorAlterarService{
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    AssociacaoRepository associacaoRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public EntregadorResponseDTO alterarEntregador(
            Long id, String codigoAcesso,
            EntregadorPostPutRequestDTO entregadorPostPutRequestDTO
    )
            throws EntregadorIdInvalidoException, EntregadorNaoExisteException, CodigoDeAcessoInvalidoException
    {
        validarID(id);

        Entregador entregador = this.entregadorRepository.findById(id)
                .orElseThrow(EntregadorNaoExisteException::new);

        validarCodigoAcesso(entregador.getCodigoAcesso(), codigoAcesso);
        modelMapper.map(entregadorPostPutRequestDTO, entregador);
        this.entregadorRepository.save(entregador);
        AtualizarAssociacoes(
                this.associacaoRepository.findAllIdsByEntregadorId(entregador.getId()),
                entregador.getCodigoAcesso()
        );
        return modelMapper.map(entregador, EntregadorResponseDTO.class);
    }

    private void validarID(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new EntregadorIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
    }

    private void AtualizarAssociacoes(List<Long> associacoesIDs, String novoCodigoAcesso) {
        if (!associacoesIDs.isEmpty()) {
            for (Long associacaoId: associacoesIDs) {
                Associacao associacao = this.associacaoRepository
                        .findById(associacaoId)
                        .orElseThrow(AssociacaoNaoExisteException::new);
                associacao.setCodigoAcesso(novoCodigoAcesso);
                this.associacaoRepository.save(associacao);
            }
        }
    }

}
