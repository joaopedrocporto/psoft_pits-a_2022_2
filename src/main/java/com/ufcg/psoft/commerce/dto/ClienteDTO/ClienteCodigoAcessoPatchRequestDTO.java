package com.ufcg.psoft.commerce.dto.ClienteDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.constraints.CodigoAcessoConstraint.CodigoAcessoConstraint;
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
public class ClienteCodigoAcessoPatchRequestDTO {

    @JsonProperty("codigoAcesso")
    @NotBlank(message = "Codigo de acesso nao pode ser vazio")
    @CodigoAcessoConstraint
    private String codigoAcesso;

}
