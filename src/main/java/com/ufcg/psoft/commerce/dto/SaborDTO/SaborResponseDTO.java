package com.ufcg.psoft.commerce.dto.SaborDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.dto.ClienteDTO.ClienteResponseDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import jakarta.validation.constraints.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaborResponseDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("valorM")
    private Double precoM;

    @JsonProperty("valorG")
    private Double precoG;

    @JsonProperty("tipo")
    private String tipo;

    @JsonProperty("disponivel")
    @Builder.Default
    private boolean disponivel = true;

    /* Nunca exponha a entidade Cliente aqui: ela carrega o codigoAcesso. */
    @JsonProperty("clientesInteressados")
    @NotNull
    private Set<ClienteResponseDTO> clientesInteressados;
}