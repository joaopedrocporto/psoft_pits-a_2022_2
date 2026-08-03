package com.ufcg.psoft.commerce.dto.EntregadorDTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.constraints.EntregadorConstraint.TipoVeiculoConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntregadorResponseDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("nome")
    @NotBlank(message = "Nome e obrigatorio")
    private String nome;

    @JsonProperty("placaVeiculo")
    @NotBlank(message = "Placa do veiculo e obrigatoria")
    private String placaVeiculo;

    @JsonProperty("corVeiculo")
    @NotBlank(message = "Cor do veiculo obrigatoria")
    private String corVeiculo;

    @JsonProperty("tipoVeiculo")
    @NotBlank(message = "Tipo de veiculo obrigatorio")
    @TipoVeiculoConstraint
    private String tipoVeiculo;

    @JsonProperty("disponibilidade")
    private String disponibilidade;


    public boolean isDisponibilidade() {
        return this.disponibilidade.equals("true");
    }
}
