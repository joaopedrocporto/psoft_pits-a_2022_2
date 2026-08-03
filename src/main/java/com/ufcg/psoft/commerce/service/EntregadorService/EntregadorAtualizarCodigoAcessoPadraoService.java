package com.ufcg.psoft.commerce.service.EntregadorService;

import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorCodigoAcessoPatchRequestDTO;
import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorGetRequestDTO;
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
public class EntregadorAtualizarCodigoAcessoPadraoService implements EntregadorAtualizarCodigoAcessoService {
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    AssociacaoRepository associacaoRepository;
    @Autowired
    ModelMapper modelMapper;


    public EntregadorGetRequestDTO atualizarCodigoAcesso(
            Long id,
            String codigoAcesso,
            EntregadorCodigoAcessoPatchRequestDTO entregadorCodigoAcessoPatchRequestDTO
    ) throws
            EntregadorIdInvalidoException, EntregadorNaoExisteException, CodigoDeAcessoInvalidoException
    {
        validarID(id);

        Entregador entregador = this.entregadorRepository.findById(id).orElseThrow(EntregadorNaoExisteException::new);
        validarCodigoAcesso(entregador.getCodigoAcesso(), codigoAcesso);
        this.modelMapper.map(entregadorCodigoAcessoPatchRequestDTO, entregador);
        AtualizarAssociacoes(
                this.associacaoRepository.findAllIdsByEntregadorId(entregador.getId()),
                entregador.getCodigoAcesso()
        );
        return this.modelMapper.map(this.entregadorRepository.save(entregador), EntregadorGetRequestDTO.class);
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

    private void validarID(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new EntregadorIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
    }

}
