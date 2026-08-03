package com.ufcg.psoft.commerce.dto.SaborDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Cliente;
import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaborPostPutRequestDTO {

    @JsonProperty("nome")
    @NotBlank(message = "Nome obrigatorio")
    private String nome;

    @JsonProperty("tipo")
    @NotBlank(message = "Tipo obrigatorio")
    private String tipo;

    @JsonProperty("valorM")
    @NotNull(message = "PrecoM obrigatorio")
    @Positive(message = "PrecoM deve ser maior que zero")
    private Double precoM;

    @JsonProperty("valorG")
    @NotNull(message = "PrecoG obrigatorio")
    @Positive(message = "PrecoG deve ser maior que zero")
    private Double precoG;

    @JsonProperty("disponivel")
    @NotNull(message = "Disponibilidade obrigatoria")
    @Builder.Default
    private Boolean disponivel = true;



}