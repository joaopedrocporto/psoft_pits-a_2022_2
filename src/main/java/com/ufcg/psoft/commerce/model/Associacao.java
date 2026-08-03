package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "associacaoes")
public class Associacao {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("status")
    @Column(nullable = false)
    private boolean status;

    @JsonProperty(value = "codigoAcesso", access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = true)
    private String codigoAcesso;

    @JsonProperty("estabelecimentoId")
    @Column(nullable = false)
    private Long estabelecimentoId;

    @JsonProperty("entregadorId")
    @Column(nullable = false)
    private Long entregadorId;

}
