package com.ufcg.psoft.commerce.service.PedidoService.PedidoClienteService;

import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoGetRequestDTO;
import com.ufcg.psoft.commerce.exception.ClienteException.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoException.EstabelecimentoNaoEncontradoException;
import com.ufcg.psoft.commerce.exception.PedidoException.PedidoNaoExisteException;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteListarPedidoEstabelecimentoPadraoService implements ClienteListarPedidoEstabelecimentoService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<PedidoGetRequestDTO> listarPedidoEstabelecimento(
            Long pedidoId, Long clienteId, Long estabelecimentoId,
            String codigoAcesso, String status
    ) throws
            ClienteNaoExisteException,
            EstabelecimentoNaoEncontradoException,
            PedidoNaoExisteException
    {
        clienteRepository.findById(clienteId).orElseThrow(ClienteNaoExisteException::new);
        estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(EstabelecimentoNaoEncontradoException::new);

        List<Pedido> pedidos = pedidoRepository.findAll();

        List<PedidoGetRequestDTO> pedidosFiltrados = pedidos.stream()
                .filter(pedido -> pedido.getClienteId().equals(clienteId) && pedido.getEstabelecimentoId().equals(estabelecimentoId))
                .map(pedido -> modelMapper.map(pedido, PedidoGetRequestDTO.class))
                .collect(Collectors.toList());

        if (FuncoesValidacao.validarId(pedidoId)) {
            Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);

            return List.of(new PedidoGetRequestDTO(pedido));
        }

        if (FuncoesValidacao.isNotEmptyOrNull(status)) {
            pedidosFiltrados.removeIf(pedido -> !pedido.getStatusEntrega().equals(status));
        }

        return ordenarPedidos(pedidosFiltrados);
    }

    private List<PedidoGetRequestDTO> ordenarPedidos(List<PedidoGetRequestDTO> pedidos) {
        return pedidos.stream()
                .sorted(Comparator.comparing((PedidoGetRequestDTO pedido) -> pedido.getStatusEntrega().equals("Pedido entregue") ? 1 : 0)
                        .thenComparing(PedidoGetRequestDTO::getStatusEntrega))
                .collect(Collectors.toList());
    }
}
