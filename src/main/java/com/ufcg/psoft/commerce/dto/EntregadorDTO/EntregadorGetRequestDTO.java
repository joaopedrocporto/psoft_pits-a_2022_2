package com.ufcg.psoft.commerce.dto.EntregadorDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.constraints.EntregadorConstraint.TipoVeiculoConstraint;
import com.ufcg.psoft.commerce.model.Entregador;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntregadorGetRequestDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("nome")
    @NotBlank(message = "Nome e obrigatorio")
    private String nome;

    @JsonProperty("placaVeiculo")
    @NotBlank(message = "Placa do veiculo e obrigatoria")
    private String placaVeiculo;

    @JsonProperty("tipoVeiculo")
    @NotBlank(message = "Tipo de veiculo obrigatorio")
    @TipoVeiculoConstraint
    private String tipoVeiculo;

    @JsonProperty("corVeiculo")
    @NotBlank(message = "Cor do veiculo obrigatoria")
    private String corVeiculo;

    public EntregadorGetRequestDTO(Entregador entregador) {
        this.id = entregador.getId();
        this.nome = entregador.getNome();
        this.placaVeiculo = entregador.getPlacaVeiculo();
        this.corVeiculo = entregador.getCorVeiculo();
        this.tipoVeiculo = entregador.getTipoVeiculo();
    }

}
