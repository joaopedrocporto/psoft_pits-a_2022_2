package com.ufcg.psoft.commerce.dto.PedidoDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.model.Pizza;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoGetRequestDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("preco")
    private Double preco;

    @JsonProperty("enderecoEntrega")
    private String enderecoEntrega;

    @JsonProperty("clienteId")
    private Long clienteId;

    @JsonProperty("estabelecimentoId")
    private Long estabelecimentoId;

    @JsonProperty("statusPagamento")
    @Builder.Default
    private Boolean statusPagamento = false;

    @JsonProperty("statusEntrega")
    @Builder.Default
    private String statusEntrega = "Pedido recebido";

    @JsonProperty("entregadorId")
    private Long entregadorId;

    @JsonProperty("pizzas")
    @Builder.Default
    private List<Pizza> pizzas = new ArrayList<>();

    public PedidoGetRequestDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.clienteId = pedido.getClienteId();
        this.estabelecimentoId = pedido.getEstabelecimentoId();
        this.enderecoEntrega = pedido.getEnderecoEntrega();
        this.preco = pedido.getPreco();
        this.statusPagamento = pedido.getStatusPagamento();
        this.statusEntrega = pedido.getStatusEntrega();
        this.pizzas = pedido.getPizzas();
        this.entregadorId = pedido.getEntregadorId();
    }
}