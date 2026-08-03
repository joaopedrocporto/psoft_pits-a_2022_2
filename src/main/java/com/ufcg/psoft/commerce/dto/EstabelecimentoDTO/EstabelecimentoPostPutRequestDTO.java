package com.ufcg.psoft.commerce.dto.EstabelecimentoDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.constraints.CodigoAcessoConstraint.CodigoAcessoConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstabelecimentoPostPutRequestDTO {

    @JsonProperty("codigoAcesso")
    @NotBlank(message = "O codigo de acesso e um parametro obrigatoio!")
    @CodigoAcessoConstraint(message = "Codigo de acesso deve ter exatamente 6 digitos numericos")
    private String codigoAcesso;

}