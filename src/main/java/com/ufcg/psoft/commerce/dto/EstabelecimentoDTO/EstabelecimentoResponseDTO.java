package com.ufcg.psoft.commerce.dto.EstabelecimentoDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.model.Sabor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstabelecimentoResponseDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("endereco")
    private String endereco;

    @JsonProperty("sabores")
    private Set<Sabor> sabores;

    @JsonProperty("pedidosPendentes")
    private List<Pedido> pedidosPendentes;

    @JsonProperty("entregadoresDisponiveis")
    private List<Entregador> entregadoresDisponiveis;
}
