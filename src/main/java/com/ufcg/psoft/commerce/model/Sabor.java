package com.ufcg.psoft.commerce.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sabores")
public class Sabor {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("nome")
    @Column(nullable = false)
    private String nome;

    @JsonProperty("tipo")
    @Column(nullable = false)
    private String tipo;

    @JsonProperty("valorM")
    @Column(nullable = false)
    private Double precoM;

    @JsonProperty("valorG")
    @Column(nullable = false)
    private Double precoG;

    @JsonProperty("disponivel")
    @Column(nullable = false)
    @Builder.Default
    private boolean disponivel = true;

    /* Sem orphanRemoval/ALL: apagar um sabor nao pode apagar os clientes interessados. */
    @JsonProperty("clientesInteressados")
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<Cliente> clientesInteressados = new HashSet<>();
}