package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.EstabelecimentoDTO.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.commerce.service.EstabelecimentoService.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/estabelecimentos",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class EstabelecimentoV1RestController {
    @Autowired
    EstabelecimentoCriarService estabelecimentoCriarService;
    @Autowired
    EstabelecimentoExcluirService estabelecimentoExcluirService;
    @Autowired
    EstabelecimentoListarService estabelecimentoListarService;
    @Autowired
    EstabelecimentoAlterarCodigoAcessoService estabelecimentoAlterarCodigoAcessoService;
    @Autowired
    EstabelecimentoMostrarOCardapioService estabelecimentoMostrarOCardapioService;
    @Autowired
    EstabelecimentoMostrarCardapioPorSaborService estabelecimentoMostrarCardapioPorSaborService;
    @Autowired
    EstabelecimentoModificarDisponibilidadeSaboresService estabelecimentoModificarDisponibilidadeSaboresService;

    @PostMapping()
    public ResponseEntity<?> criarEstabelecimento(
            @RequestBody @Valid EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(estabelecimentoCriarService.criar(estabelecimentoPostPutRequestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarEstabelecimento(
            @PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoListarService.get(id));
    }

    @GetMapping()
    public ResponseEntity<?> buscarEstabelecimentos() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoListarService.get(null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> alterarCodigoDeAcessoEstabelecimento(
            @PathVariable Long id,
            @RequestParam String codigoAcesso,
            @RequestBody @Valid EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoAlterarCodigoAcessoService.atualizarCodigo(id, codigoAcesso, estabelecimentoPostPutRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirEstabelecimento(
            @PathVariable Long id,
            @RequestParam String codigoAcesso
    ) {
        estabelecimentoExcluirService.excluir(id, codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @GetMapping("/{id}/sabores")
    public ResponseEntity<?> mostrarCardapio(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoMostrarOCardapioService.mostrar(id));
    }

    @GetMapping("/{id}/sabores/tipo")
    public ResponseEntity<?> mostrarCardapioPorTipo(@PathVariable Long id, String tipo){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoMostrarCardapioPorSaborService.mostrar(id, tipo));
    }

    @PatchMapping("/{idEstabelecimento}/{idSabor}")
    public ResponseEntity<?> modificarDisponibilidadeSabores(@PathVariable Long idEstabelecimento, @PathVariable Long idSabor, @RequestParam Boolean disponibilidade) {
        return ResponseEntity
                .status((HttpStatus.OK))
                .body(estabelecimentoModificarDisponibilidadeSaboresService.modificarDisponibilidadeSabor(idEstabelecimento,idSabor,disponibilidade));
    }
}