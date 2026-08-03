package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clientes")
public class Cliente {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("nome")
    @Column(nullable = false)
    private String nome;

    @JsonProperty("endereco")
    @Column(nullable = false)
    private String endereco;

    @JsonProperty(value = "codigoAcesso", access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String codigoAcesso;

    public boolean isEquals(Cliente cliente) {
        if (FuncoesValidacao.isNull(cliente)) return false;
        return this.nome.equals(cliente.getNome()) &&
                this.codigoAcesso.equals(cliente.codigoAcesso) &&
                this.endereco.equals(cliente.getEndereco());
    }

}
