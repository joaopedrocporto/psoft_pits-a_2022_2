package com.ufcg.psoft.commerce.dto.PedidoDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Pizza;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoPostPutRequestDTO {

    @JsonProperty("enderecoEntrega")
    private String enderecoEntrega;

    @JsonProperty("pizzas")
    @NotEmpty(message = "Pizzas obrigatorias")
    private List<Pizza> pizzas;

}
