package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.SaborDTO.SaborPostPutRequestDTO;
import com.ufcg.psoft.commerce.service.SaborService.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/sabores", produces = MediaType.APPLICATION_JSON_VALUE
)
public class SaborV1RestController {
    @Autowired
    CriarSaborService criarSaborService;

    @Autowired
    ExibirSaborService exibirSaborService;

    @Autowired
    EditarSaborService editarSaborService;

    @Autowired
    DeletarSaborService deletarSaborService;

    @Autowired
    EditarSaborDisponibilidadeService editarSaborDisponibilidadeService;

    @PostMapping()
    public ResponseEntity<?> criarSabor(
            @RequestBody @Valid SaborPostPutRequestDTO saborPostPutRequestDTO,
            @RequestParam Long estabelecimentoId,
            @RequestParam String estabelecimentoCodigoAcesso
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.criarSaborService.criar(saborPostPutRequestDTO, estabelecimentoId, estabelecimentoCodigoAcesso));
    }

    @PutMapping()
    public ResponseEntity<?> atualizarSabor(
            @RequestParam Long saborId,
            @RequestBody @Valid SaborPostPutRequestDTO saborPostPutRequestDto,
            @RequestParam Long estabelecimentoId,
            @RequestParam String estabelecimentoCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.editarSaborService.editar(saborPostPutRequestDto, saborId, estabelecimentoId, estabelecimentoCodigoAcesso));
    }

    @PutMapping("/{saborId}/{disponibilidade}")
    public ResponseEntity<?> atualizarDisponibilidadeSabor(
            @PathVariable Long saborId,
            @PathVariable boolean disponibilidade,
            @RequestParam Long estabelecimentoId,
            @RequestParam String estabelecimentoCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.editarSaborDisponibilidadeService.editarDisponibilidade(saborId, estabelecimentoId, estabelecimentoCodigoAcesso, disponibilidade));
    }

    @GetMapping("/{saborId}")
    public ResponseEntity<?> buscarSabor(
            @PathVariable Long saborId,
            @RequestParam Long estabelecimentoId,
            @RequestParam String estabelecimentoCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.exibirSaborService.listar(saborId, estabelecimentoId, estabelecimentoCodigoAcesso));
    }

    @GetMapping()
    public ResponseEntity<?> buscarSabores(
            @RequestParam Long estabelecimentoId,
            @RequestParam String estabelecimentoCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.exibirSaborService.listar(null, estabelecimentoId, estabelecimentoCodigoAcesso));
    }

    @DeleteMapping()
    public ResponseEntity<?> excluirSabor(
            @RequestParam Long saborId,
            @RequestParam Long estabelecimentoId,
            @RequestParam String estabelecimentoCodigoAcesso
    ) {
        this.deletarSaborService.deletar(saborId, estabelecimentoId, estabelecimentoCodigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }
}