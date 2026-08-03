package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.service.AssociacaoService.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/associacao",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class AssociacaoV1RestController {
    @Autowired
    AssociacaoAtualizarService associacaoAtualizarService;
    @Autowired
    AssociacaoAtualizarStatusService associacaoAtualizarStatusService;
    @Autowired
    AssociacaoCriarService associacaoCriarService;
    @Autowired
    AssociacaoDeletarPorAssociacaoService associacaoDeletarPorAssociacaoService;
    @Autowired
    AssociacaoDeletarPorIdService associacaoDeletarPorIdService;
    @Autowired
    AssociacaoExibirService associacaoExibirService;
    @Autowired
    AssociacaoExibirTodosService associacaoExibirTodosService;

    @PostMapping()
    public ResponseEntity<?> criarAssociacao(
            @RequestParam Long entregadorId,
            @RequestParam String codigoAcesso,
            @RequestParam Long estabelecimentoId
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.associacaoCriarService.criar(entregadorId, codigoAcesso, estabelecimentoId));
    }

    @PutMapping()
    public ResponseEntity<?> atualizarAssociacao(
            @RequestParam Long entregadorId,
            @RequestParam String codigoAcesso,
            @RequestParam Long estabelecimentoId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.associacaoAtualizarService.atualizar(entregadorId, codigoAcesso, estabelecimentoId));
    }

    @GetMapping("/exibir")
    public ResponseEntity<?> exibir(
            @RequestParam Long entregadorId,
            @RequestParam String codigoAcesso,
            @RequestParam Long estabelecimentoId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.associacaoExibirService.exbir(entregadorId, codigoAcesso, estabelecimentoId));
    }

    @GetMapping()
    public ResponseEntity<?> exibirTodos(
            @RequestParam Long entregadorId,
            @RequestParam String codigoAcesso
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.associacaoExibirTodosService.exibirTodos(entregadorId, codigoAcesso));
    }

    @PatchMapping()
    public ResponseEntity<?> atualizarStatus(
            @RequestParam Long entregadorId,
            @RequestParam Long estabelecimentoId,
            @RequestParam String codigoAcesso
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.associacaoAtualizarStatusService
                        .atualizarStatus(entregadorId, estabelecimentoId, codigoAcesso));
    }

    @DeleteMapping("/{associacaoId}")
    public ResponseEntity<?> deletarPorId(
            @PathVariable Long associacaoId,
            @RequestParam String codigoAcesso
    ) {
        this.associacaoDeletarPorIdService.deletarPorID(associacaoId, codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @DeleteMapping()
    public ResponseEntity<?> deletar(
            @RequestParam Long entregadorId,
            @RequestParam String codigoAcesso,
            @RequestParam Long estabelecimentoId
    ) {
       this.associacaoDeletarPorAssociacaoService.deletar(entregadorId, codigoAcesso, estabelecimentoId);
       return ResponseEntity
               .status(HttpStatus.NO_CONTENT)
               .body("");
    }

}
