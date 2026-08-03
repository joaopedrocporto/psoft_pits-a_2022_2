package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.EntregadorDTO.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.service.EntregadorService.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/entregadores",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class EntregadorV1RestController {
    @Autowired
    EntregadorCriarService entregadorCriarService;
    @Autowired
    EntregadorBuscarService entregadorBuscarService;
    @Autowired
    EntregadorListarService entregadorListarService;
    @Autowired
    EntregadorDeletarService entregadorDeletarService;
    @Autowired
    EntregadorAlterarService entregadorAlterarService;
    @Autowired
    EntregadorAlterarDisponibilidadeService entregadorAlterarDisponibilidadeService;

    @PostMapping
    public ResponseEntity<?> salvarEntregador(@RequestBody @Valid EntregadorPostPutRequestDTO entregadorPostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.entregadorCriarService.salvar(entregadorPostPutRequestDTO));
    }

    @GetMapping
    public ResponseEntity<?> listarEntregadores() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.entregadorListarService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarEntregador(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.entregadorBuscarService.buscar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarEntregador(@PathVariable Long id, @RequestParam String codigoAcesso) {
        this.entregadorDeletarService.deletarEntregador(id, codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> alterarEntregador(
            @PathVariable Long id,
            @RequestParam String codigoAcesso,
            @RequestBody @Valid EntregadorPostPutRequestDTO entregadorPostPutRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.entregadorAlterarService.alterarEntregador(id, codigoAcesso, entregadorPostPutRequestDTO));
    }

    @PutMapping("/{id}/disponibilidade")
    public ResponseEntity<?> alterarDisponibilidade(
            @PathVariable Long id,
            @RequestParam String codigoAcesso,
            @RequestParam boolean disponibilidade
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.entregadorAlterarDisponibilidadeService.alterarDisponibilidade(id, codigoAcesso, disponibilidade));
    }

}
