package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.ClienteDTO.ClientePostPutRequestDTO;
import com.ufcg.psoft.commerce.service.ClienteService.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping(
        value = "/clientes",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ClienteV1RestController {
    @Autowired
    ClienteCriarService clienteCriarService;
    @Autowired
    ClienteDeletarService clienteExcluirService;
    @Autowired
    ClienteAlteraService clienteAlterarService;
    @Autowired
    ClienteBuscarService clienteBuscarPadraoService;
    @Autowired
    ClienteBuscarTodosService clienteBuscarTodosService;
    @Autowired
    ClienteDemostrarInteresseService clienteDemostrarInteresseService;

    @PostMapping()
    public ResponseEntity<?> criarCliente(
            @RequestBody @Valid ClientePostPutRequestDTO clientePostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteCriarService.criar(clientePostPutRequestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarCliente(
            @PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.clienteBuscarPadraoService.buscar(id));
    }

    @GetMapping()
    public ResponseEntity<?> buscarTodosCliente() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.clienteBuscarTodosService.buscarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> alterarCliente(
            @PathVariable Long id,
            @RequestParam String codigoAcesso,
            @RequestBody @Valid ClientePostPutRequestDTO clientePostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteAlterarService.alterar(id, clientePostPutRequestDTO, codigoAcesso));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirCliente(
            @PathVariable Long id,
            @RequestParam String codigoAcesso) {
        clienteExcluirService.deletar(id, codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }


    @PutMapping("/{id}/demonstrarInteresse")
    public ResponseEntity<?> demonstrarInteresse(
            @PathVariable Long id,
            @RequestParam String codigoAcesso,
            @RequestParam Long saborId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteDemostrarInteresseService.interessar(id,codigoAcesso,saborId));
    }

}