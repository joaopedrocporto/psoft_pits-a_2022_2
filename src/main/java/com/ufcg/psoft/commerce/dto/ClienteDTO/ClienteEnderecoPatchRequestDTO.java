package com.ufcg.psoft.commerce.dto.ClienteDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteEnderecoPatchRequestDTO {

    @JsonProperty("endereco")
    @NotBlank(message = "Deve ter um endereco")
    private String endereco;

}
