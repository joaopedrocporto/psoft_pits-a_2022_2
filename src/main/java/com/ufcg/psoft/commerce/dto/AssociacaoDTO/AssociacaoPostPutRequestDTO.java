package com.ufcg.psoft.commerce.dto.AssociacaoDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssociacaoPostPutRequestDTO {

    @JsonProperty("status")
    @Builder.Default
    private boolean status = false;

    @JsonProperty("codigoAcesso")
    @NotBlank(message = "Deve ter um codigo de acesso")
    private String codigoAcesso;

}
