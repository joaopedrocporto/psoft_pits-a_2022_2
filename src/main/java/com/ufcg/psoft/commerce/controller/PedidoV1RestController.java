package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.PedidoDTO.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.service.PedidoService.*;

import com.ufcg.psoft.commerce.service.PedidoService.PedidoClienteService.*;
import com.ufcg.psoft.commerce.service.PedidoService.PedidoEstabelecimentoService.EstabelecimentoAssociacaoPedidoEntregadorService;
import com.ufcg.psoft.commerce.service.PedidoService.PedidoEstabelecimentoService.EstabelecimentoExcluirPedidoService;
import com.ufcg.psoft.commerce.service.PedidoService.PedidoEstabelecimentoService.EstabelecimentoListarPedidoService;
import com.ufcg.psoft.commerce.service.PedidoService.PedidoEstabelecimentoService.EstabelecimentoPrepararPedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/pedidos", produces = MediaType.APPLICATION_JSON_VALUE
)
public class PedidoV1RestController {
    @Autowired
    PedidoCriarService pedidoCriarService;
    @Autowired
    PedidoCancelarPorClienteService pedidoCancelarPorClienteService;
    @Autowired
    PedidoEditarService pedidoEditarService;
    @Autowired
    ClienteListarPedidoService clienteListarPedidoService;
    @Autowired
    PedidoConfirmarPagamentoService pedidoConfirmarPagamentoService;
    @Autowired
    ClienteExcluirPedidoService clienteExcluirPedidoService;
    @Autowired
    ClienteListarPedidoEstabelecimentoService clienteListarPedidoEstabelecimentoService;
    @Autowired
    EstabelecimentoListarPedidoService estabelecimentoListarPedidoServiceService;
    @Autowired
    EstabelecimentoExcluirPedidoService estabelecimentoExcluirPedidoService;
    @Autowired
    PedidoConfirmadoPorClienteService pedidoConfirmadoPorClienteService;
    @Autowired
    EstabelecimentoPrepararPedidoService estabelecimentoPrepararPedidoService;
    @Autowired
    EstabelecimentoAssociacaoPedidoEntregadorService estabelecimentoAssociacaoPedidoEntregadorService;
    @Autowired
    ClienteVisualizarPedidoService clienteVisualizarPedidoService;
    @Autowired
    ClienteVisualizarHistoricoPedidosService clienteVisualizarHistoricoPedidosService;
    @Autowired
    ClienteFiltrarPedidosPorStatusService clienteFiltrarPedidosPorStatusService;

    @PostMapping()
    public ResponseEntity<?> criarPedido(
            @RequestParam Long clienteId,
            @RequestParam String clienteCodigoAcesso,
            @RequestParam Long estabelecimentoId,
            @RequestBody @Valid PedidoPostPutRequestDTO pedidoPostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedidoCriarService.criar(clienteId, clienteCodigoAcesso, estabelecimentoId, pedidoPostPutRequestDto));
    }

    @PutMapping()
    public ResponseEntity<?> editarPedido(
            @RequestParam Long pedidoId,
            @RequestParam String codigoAcesso,
            @RequestBody @Valid PedidoPostPutRequestDTO pedidoPostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoEditarService.editar(pedidoId, codigoAcesso, pedidoPostPutRequestDto));
    }

    @GetMapping("/{pedidoId}/{clienteId}")
    public ResponseEntity<?> listarPedido(
            @PathVariable Long pedidoId,
            @PathVariable Long clienteId,
            @RequestParam String codigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteListarPedidoService.listar(pedidoId, clienteId, codigoAcesso));
    }

    @DeleteMapping("/{pedidoId}/cancelar-pedido")
    public ResponseEntity<?> cancelarPedido(
            @PathVariable Long pedidoId,
            @RequestParam String clienteCodigoAcesso) {
        pedidoCancelarPorClienteService.cancelar(pedidoId, clienteCodigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @GetMapping("/pedido-cliente-estabelecimento/{clienteId}/{estabelecimentoId}/{pedidoId}")
    public ResponseEntity<?> clienteListarPedidoFeitoEmEstabelecimento(
            @PathVariable Long pedidoId,
            @PathVariable Long clienteId,
            @PathVariable Long estabelecimentoId,
            @RequestParam String clienteCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        clienteListarPedidoEstabelecimentoService
                                .listarPedidoEstabelecimento(
                                        pedidoId,
                                        clienteId,
                                        estabelecimentoId,
                                        clienteCodigoAcesso,
                                        null
                                )
                );
    }

    @GetMapping("/pedidos-cliente-estabelecimento/{clienteId}/{estabelecimentoId}")
    public ResponseEntity<?> clienteListarTodosPedidosFeitosEmEstabelecimento(
            @PathVariable Long clienteId,
            @PathVariable Long estabelecimentoId,
            @RequestParam String clienteCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        clienteListarPedidoEstabelecimentoService
                                .listarPedidoEstabelecimento(
                                        null,
                                        clienteId,
                                        estabelecimentoId,
                                        clienteCodigoAcesso,
                                        null
                                )
                );
    }

    @GetMapping("/pedidos-cliente-estabelecimento/{clienteId}/{estabelecimentoId}/{status}")
    public ResponseEntity<?> clienteListarTodosPedidosFeitosEmEstabelecimentoPorStatus(
            @PathVariable Long clienteId,
            @PathVariable Long estabelecimentoId,
            @PathVariable String status,
            @RequestParam String clienteCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        clienteListarPedidoEstabelecimentoService
                                .listarPedidoEstabelecimento(
                                        null,
                                        clienteId,
                                        estabelecimentoId,
                                        clienteCodigoAcesso,
                                        status
                                )
                );
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<?> listarTodosPedidosCliente(
            @PathVariable Long clienteId,
            @RequestParam String codigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteListarPedidoService.listar(null, clienteId, codigoAcesso));
    }

    @DeleteMapping("/{pedidoId}/{clienteId}")
    public ResponseEntity<?> clienteExcluirPedidoSalvo(
            @PathVariable Long pedidoId,
            @PathVariable Long clienteId,
            @RequestParam String codigoAcesso
    ) {
        clienteExcluirPedidoService.excluir(pedidoId, clienteId, codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<?> clienteExcluirTodosSalvos(
            @PathVariable Long clienteId,
            @RequestParam String codigoAcesso) {
        clienteExcluirPedidoService.excluir(null, clienteId, codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @GetMapping("/estabelecimento/{estabelecimentoId}")
    public ResponseEntity<?> estabelecimentoListarTodosPedidosSalvos(
            @PathVariable Long estabelecimentoId,
            @RequestParam String codigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoListarPedidoServiceService.listar(null, estabelecimentoId, codigoAcesso));
    }

    @GetMapping("/estabelecimento/{pedidoId}/{estabelecimentoId}")
    public ResponseEntity<?> estabelecimentoListarPedidoSalvos(
            @PathVariable Long pedidoId,
            @PathVariable Long estabelecimentoId,
            @RequestParam String codigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoListarPedidoServiceService.listar(pedidoId, estabelecimentoId, codigoAcesso));
    }

    @DeleteMapping("/estabelecimento/{pedidoId}/{estabelecimentoId}")
    public ResponseEntity<?> estabelecimentoExcluirPedidoSalvos(
            @PathVariable Long estabelecimentoId,
            @PathVariable Long pedidoId,
            @RequestParam String codigoAcesso) {
        estabelecimentoExcluirPedidoService.excluir(pedidoId, estabelecimentoId, codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @DeleteMapping("/estabelecimento/{estabelecimentoId}")
    public ResponseEntity<?> estabelecimentoExcluirTodosPedidosSalvos(
            @PathVariable Long estabelecimentoId,
            @RequestParam String codigoAcesso) {
        estabelecimentoExcluirPedidoService.excluir(null, estabelecimentoId, codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @PutMapping("/confirmar-pagamento")
    public ResponseEntity<?> confirmarPagamento(
            @RequestParam Long pedidoId,
            @RequestParam String metodoPagamento,
            @RequestParam String codigoAcessoCliente) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoConfirmarPagamentoService.confirmarPagamento(pedidoId, metodoPagamento, codigoAcessoCliente));
    }

    @PutMapping("/{pedidoId}/{clienteId}/cliente-confirmar-entrega")
    public ResponseEntity<?> confirmarEntrega(
            @PathVariable Long pedidoId,
            @PathVariable Long clienteId,
            @RequestParam String clienteCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoConfirmadoPorClienteService.confirmar(pedidoId, clienteId, clienteCodigoAcesso));
    }

    @PutMapping("/{pedidoId}/associar-pedido-entregador")
    public ResponseEntity<?> estabelecimentoAssociarPedidoEntregador(
            @PathVariable Long pedidoId,
            @RequestParam Long estabelecimentoId,
            @RequestParam String codigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.estabelecimentoAssociacaoPedidoEntregadorService.associarPedidoEntregador(pedidoId,estabelecimentoId,codigoAcesso));
    }

    @PutMapping("/estabelecimento/{estabelecimentoId}/{pedidoId}")
    public ResponseEntity<?> estabelecimentoPrepararPedido(
            @PathVariable Long pedidoId,
            @PathVariable Long estabelecimentoId,
            @RequestParam String codigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.estabelecimentoPrepararPedidoService.prepararPedido(pedidoId,estabelecimentoId,codigoAcesso));
    }

    @GetMapping("/{clienteId}/{pedidoId}/cliente-visualizar-pedido")
    public ResponseEntity<?> visualizarPedido(
            @PathVariable Long pedidoId,
            @PathVariable Long clienteId,
            @RequestParam String codigoAcesso){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteVisualizarPedidoService.visualizar(pedidoId,clienteId,codigoAcesso));
    }

    @GetMapping("/historico/{idCliente}")
    public ResponseEntity<?> visualizarHistoricoPedido(
            @PathVariable Long idCliente,
            @RequestParam String codigoAcessoCliente){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteVisualizarHistoricoPedidosService.visualizarHistorico(idCliente, codigoAcessoCliente));
    }

    @GetMapping("/filtrar/{idCliente}/{statusEntrega}")
    public ResponseEntity<?> filtrarPedidosPorStatus(
            @PathVariable Long idCliente,
            @PathVariable String statusEntrega,
            @RequestParam String codigoAcessoCliente){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteFiltrarPedidosPorStatusService.filtrarPorStatus(idCliente, statusEntrega, codigoAcessoCliente));
    }
}