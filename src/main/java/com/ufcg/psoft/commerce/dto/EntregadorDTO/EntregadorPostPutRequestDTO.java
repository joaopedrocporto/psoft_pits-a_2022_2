package com.ufcg.psoft.commerce.dto.EntregadorDTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.constraints.CodigoAcessoConstraint.CodigoAcessoConstraint;
import com.ufcg.psoft.commerce.constraints.EntregadorConstraint.TipoVeiculoConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntregadorPostPutRequestDTO {

    @JsonProperty("nome")
    @NotBlank(message = "Nome e obrigatorio")
    private String nome;

    @JsonProperty("placaVeiculo")
    @NotBlank(message = "Placa do veiculo e obrigatoria")
    private String placaVeiculo;

    @JsonProperty("tipoVeiculo")
    @NotBlank(message = "Tipo do veiculo e obrigatorio")
    @TipoVeiculoConstraint
    private String tipoVeiculo;

    @JsonProperty("corVeiculo")
    @NotBlank(message = "Cor do veiculo e obrigatoria")
    private String corVeiculo;

    @JsonProperty("codigoAcesso")
    @NotBlank(message = "Codigo de acesso obrigatorio")
    @CodigoAcessoConstraint
    private String codigoAcesso;
    
}
