package com.ufcg.psoft.commerce.service.PedidoService;

import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.exception.ClienteException.ClienteIdInvalidoException;
import com.ufcg.psoft.commerce.exception.ClienteException.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoAcessoException.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoIdInvalidoException;
import com.ufcg.psoft.commerce.exception.SaborException.MuitosSaboresException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborIndisponivelException;
import com.ufcg.psoft.commerce.exception.SaborException.SaborInexistenteException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.model.Pizza;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.repository.SaborRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PedidoCriarPadraoService implements PedidoCriarService {
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    SaborRepository saborRepository;
    @Autowired
    ModelMapper modelMapper;

    public Pedido criar(
            Long clienteId, String clienteCodigoAcesso,
            Long estabelecimentoId, PedidoPostPutRequestDTO pedidoPostPutRequestDTO
    ) throws
            ClienteNaoExisteException,
            SaborInexistenteException,
            EstabelecimentoNaoEncontradoException,
            CodigoDeAcessoInvalidoException
    {
        validarIdCliente(clienteId);
        validarIdEstabelecimento(estabelecimentoId);

        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(ClienteNaoExisteException::new);
        this.validarAtores(cliente, estabelecimentoId, clienteCodigoAcesso);

        if (pedidoPostPutRequestDTO.getEnderecoEntrega() == null ||
            pedidoPostPutRequestDTO.getEnderecoEntrega().isEmpty()) {
            pedidoPostPutRequestDTO.setEnderecoEntrega(cliente.getEndereco());
        }

        Pedido pedido = modelMapper.map(pedidoPostPutRequestDTO, Pedido.class);

        pedido.setPreco(this.calculaPrecoTotal(pedido.getPizzas()));
        pedido.setClienteId(clienteId);
        pedido.setEstabelecimentoId(estabelecimentoId);
        // pizzas de um pedido novo sao sempre novas: um id vindo do cliente tornaria a
        // entidade detached e quebraria o cascade de persist.
        List<Pizza> pizzasNovas = new ArrayList<>();
        for (Pizza pizza : pedidoPostPutRequestDTO.getPizzas()) {
            pizza.setId(null);
            pizzasNovas.add(pizza);
        }
        pedido.setPizzas(pizzasNovas);
        pedido.setStatusEntrega("Pedido recebido");

        return  pedidoRepository.save(pedido);
    }

    private Double calculaPrecoTotal(List<Pizza> pizzas) {
        double total = 0;

        for (Pizza pizza : pizzas) {
            this.validarPizza(pizza);
            Sabor sabor1 = saborRepository.findById(pizza.getSabor1()).orElseThrow(SaborInexistenteException::new);
            if (pizza.getTamanho().equals("media")) {
                total += sabor1.getPrecoM();
            } else if (pizza.getTamanho().equals("grande") && pizza.getSabor2() != null) {
                Sabor sabor2 = saborRepository.findById(pizza.getSabor2()).orElseThrow(SaborInexistenteException::new);
                total += (sabor1.getPrecoG() + sabor2.getPrecoG()) / 2;
            } else {
                total += sabor1.getPrecoG();
            }
        }

        return total;
    }

    private void validarPizza(Pizza pizza) {
        Sabor sabor1 =  saborRepository.findById(pizza.getSabor1()).orElseThrow(SaborInexistenteException::new);
        if (!sabor1.isDisponivel()) {
            throw new SaborIndisponivelException();
        }

        if (pizza.getSabor2() != null) {
            if (pizza.getTamanho().equals("media")) {
                throw new MuitosSaboresException();
            }

            Sabor sabor2 = saborRepository.findById(pizza.getSabor2()).orElseThrow(SaborInexistenteException::new);
            if (!sabor2.isDisponivel()) {
                throw new SaborIndisponivelException();
            }
        }
    }

    private void validarAtores(Cliente cliente, Long estabelecimentoId, String clienteCodigoAcesso) {
        estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(EstabelecimentoNaoEncontradoException::new);

        if (!cliente.getCodigoAcesso().equals(clienteCodigoAcesso)) throw new CodigoDeAcessoInvalidoException();
    }

    private void validarIdCliente(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new ClienteIdInvalidoException();
    }

    private void validarIdEstabelecimento(Long id) {
        if (!FuncoesValidacao.validarId(id)) throw new EstabelecimentoIdInvalidoException();
    }
}
