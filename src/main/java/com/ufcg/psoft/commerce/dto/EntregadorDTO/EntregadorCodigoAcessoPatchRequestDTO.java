package com.ufcg.psoft.commerce.dto.EntregadorDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.constraints.CodigoAcessoConstraint.CodigoAcessoConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntregadorCodigoAcessoPatchRequestDTO {

    @JsonProperty("codigoAcesso")
    @NotBlank(message = "Codigo de acesso nao pode ser vazio")
    @CodigoAcessoConstraint
    private String codigoAcesso;

}
