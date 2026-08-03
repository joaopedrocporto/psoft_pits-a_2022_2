package com.ufcg.psoft.commerce.service.EntregadorService;

import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorEmRotaNaoPodeSerApagadoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorNaoExisteException;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.AssociacaoRepository;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EntregadorDeletarPadraoService implements EntregadorDeletarService{
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    AssociacaoRepository associacaoRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public void deletarEntregador(Long id, String codigoAcesso) throws EntregadorIdInvalidoException,
            EntregadorNaoExisteException, CodigoDeAcessoInvalidoException
    {
        validarID(id);

        Entregador entregador = this.entregadorRepository.findById(id)
                .orElseThrow(EntregadorNaoExisteException::new);

        validarCodigoAcesso(entregador.getCodigoAcesso(), codigoAcesso);
        validarDelecao(entregador.getDisponibilidade());
        removerAssociacoes(this.associacaoRepository.findAllIdsByEntregadorId(entregador.getId()));
        this.entregadorRepository.deleteById(id);
    }

    private void validarDelecao(boolean disponibilidade) {
        if (!disponibilidade) throw new EntregadorEmRotaNaoPodeSerApagadoException();
    }

    private void removerAssociacoes(List<Long> associacoesIds) {
        if (!associacoesIds.isEmpty()) this.associacaoRepository.deleteAllById(associacoesIds);
    }

    private void validarID(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new EntregadorIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
    }

}
