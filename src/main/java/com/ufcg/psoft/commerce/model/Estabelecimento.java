package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "estabelecimentos")
public class Estabelecimento {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty(value = "codigoAcesso", access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String codigoAcesso;

    @JsonProperty("sabores")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @JoinColumn(name = "sabores")
    private Set<Sabor> sabores = new HashSet<>();

    /* Sem orphanRemoval: tirar um pedido da fila de pendentes nao pode apaga-lo do banco. */
    @JsonProperty("pedidosPendentes")
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @JoinColumn(name = "pedidos")
    private List<Pedido> pedidosPendentes = new LinkedList<>();

    /* Sem orphanRemoval: tirar um entregador da lista de disponiveis nao pode apaga-lo. */
    @JsonProperty("entregadoresDisponiveis")
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @JoinColumn(name = "entregadores")
    private List<Entregador> entregadoresDisponiveis = new LinkedList<>();

    public void addEntregador(Entregador entregador) {
        if (FuncoesValidacao.isNotNull(entregador)) this.entregadoresDisponiveis.add(entregador);
    }

    public void removerEntregador(Entregador entregador) {
        if (FuncoesValidacao.isNotNull(entregador)) this.entregadoresDisponiveis.remove(entregador);
    }

    public Entregador atribuirEntregador() {
        if (this.entregadoresDisponiveis.isEmpty()) return null;
        return this.entregadoresDisponiveis.remove(0);
    }

    public boolean verificarEntregadores() {return !this.entregadoresDisponiveis.isEmpty();}

    public boolean verificarPedidosPendentes() {return !this.pedidosPendentes.isEmpty();}

    public void addPedido(Pedido pedido) {
        if (FuncoesValidacao.isNotNull(pedido)) this.pedidosPendentes.add(pedido);
    }

    public void removerPedido(Pedido pedido) {
        if (FuncoesValidacao.isNotNull(pedido)) this.pedidosPendentes.remove(pedido);
    }

    public String notifica(Pedido pedido){
        return  "Estabelecimento de ID " +this.getId() + " o seu pedido de ID " + pedido.getId() + "foi entregue";
    }

}