package com.ufcg.psoft.commerce.service.EntregadorService;

import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorResponseDTO;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorDisponibilidadeInvalidaException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorException.EntregadorNaoExisteException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EntregadorAlterarDisponibilidadeIgualException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoNaoExisteException;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.AssociacaoRepository;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.service.PedidoService.PedidoEstabelecimentoService.EstabelecimentoAssociacaoPedidoEntregadorService;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class EntregadorAlterarDisponibilidadePadraoService implements EntregadorAlterarDisponibilidadeService{
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    AssociacaoRepository associacaoRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    EstabelecimentoAssociacaoPedidoEntregadorService estabelecimentoAssociacaoPedidoEntregadorService;

    @Override
    public EntregadorResponseDTO alterarDisponibilidade(Long id, String codigoAcesso, boolean disponibilidade)
            throws CodigoDeAcessoInvalidoException, EntregadorNaoExisteException, EntregadorIdInvalidoException,
            EntregadorAlterarDisponibilidadeIgualException
    {
        validarID(id);

        Entregador entregador = this.entregadorRepository.findById(id).orElseThrow(EntregadorNaoExisteException::new);

        validarCodigoAcesso(entregador.getCodigoAcesso(), codigoAcesso);

        List<Long> estabelecimentosEntregador = this.associacaoRepository.retornarEstabelecimentosComAssociacaoEntregador(id);

        if(entregador.getDisponibilidade() == disponibilidade){
            throw new EntregadorAlterarDisponibilidadeIgualException();
        }else{
            if(disponibilidade) {
                for (int i = 0; i < estabelecimentosEntregador.size(); i++) {
                    Estabelecimento estab = this.estabelecimentoRepository.findById(estabelecimentosEntregador.get(i))
                            .orElseThrow(EstabelecimentoIdInvalidoException::new);
                    estab.addEntregador(entregador);
                    this.estabelecimentoRepository.save(estab);
                }
            } else{
                for (int i = 0; i < estabelecimentosEntregador.size(); i++) {
                    Estabelecimento estab = this.estabelecimentoRepository.findById(estabelecimentosEntregador.get(i))
                            .orElseThrow(EstabelecimentoIdInvalidoException::new);
                    estab.removerEntregador(entregador);
                    this.estabelecimentoRepository.save(estab);
                }
            }

            entregador.setDisponibilidade(disponibilidade);
            this.entregadorRepository.save(entregador);

            if (this.verificarExistePedidoPendente(estabelecimentosEntregador) && disponibilidade == true) {
                Pedido pedidoMaisAntigo = pedidoRepository.findById(this.getPedidoMaisAntigo(estabelecimentosEntregador))
                        .orElseThrow(PedidoNaoExisteException::new);
                Estabelecimento estabelecimento = estabelecimentoRepository.findById(pedidoMaisAntigo.getEstabelecimentoId())
                        .orElseThrow(EstabelecimentoNaoExisteException::new);
                estabelecimentoAssociacaoPedidoEntregadorService.associarPedidoEntregador(
                        pedidoMaisAntigo.getId(),
                        pedidoMaisAntigo.getEstabelecimentoId(),
                        estabelecimento.getCodigoAcesso()
                );
                entregador.setDisponibilidade(false);
                this.entregadorRepository.save(entregador);
            }

            return this.modelMapper.map(entregador, EntregadorResponseDTO.class);
        }
    }

    private Long getPedidoMaisAntigo(List<Long> estabelecimentosEntregador) {
        Long pedidoMaisAntigo = Long.MAX_VALUE;

        for (Long estabelecimentoId : estabelecimentosEntregador) {
            Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId)
                    .orElseThrow(EstabelecimentoNaoExisteException::new);
            List<Pedido> pedidos = estabelecimento.getPedidosPendentes();
            for (Pedido pedido : pedidos) {
                Long pedidoId = pedido.getId();

                if (pedidoId < pedidoMaisAntigo) {
                    pedidoMaisAntigo = pedidoId;
                }
            }
        }

        return pedidoMaisAntigo;
    }

    private boolean verificarExistePedidoPendente(List<Long> estabelecimentosEntregador) {
        for (Long estabelecimentoId : estabelecimentosEntregador) {
            Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId)
                    .orElseThrow(EstabelecimentoNaoExisteException::new);
            if (estabelecimento.verificarPedidosPendentes()) {
                return true;
            }
        }

        return false;
    }

    private void validarID(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new EntregadorIdInvalidoException();
    }

    private void validarCodigoAcesso(String codigoEsperado, String codigoPassado) {
        if (!UtilCodigoAcesso.validarCodigo(codigoEsperado, codigoPassado)) throw new CodigoDeAcessoInvalidoException();
    }
}