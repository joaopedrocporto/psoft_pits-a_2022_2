package com.ufcg.psoft.commerce.repository;


import com.ufcg.psoft.commerce.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    @Query("SELECT p.id FROM Pedido p WHERE p.estabelecimentoId = :estabelecimentoId")
    List<Long> retornarPedidosEstabelecimento(@Param("estabelecimentoId") Long estabelecimentoId);

    @Query("SELECT p.id FROM Pedido p WHERE p.clienteId = :clienteId")
    List<Long> retornarPedidosCliente(@Param("clienteId") Long clienteId);

    @Query("SELECT p.id FROM Pedido p WHERE p.clienteId = :clienteId AND p.statusEntrega = :statusEntrega")
    List<Long> retornarPedidosClientePorStatusEntrega(@Param("clienteId") Long clienteId, @Param("statusEntrega") String statusEntrega);

    @Query("SELECT p.id FROM Pedido p WHERE p.clienteId = :clienteId AND p.statusEntrega = 'Pedido entregue' ORDER BY p.id DESC")
    List<Long> retornarPedidosEntregues(@Param("clienteId") Long clienteId);

    @Query("SELECT p.id FROM Pedido p WHERE p.clienteId = :clienteId AND p.statusEntrega != 'Pedido entregue' ORDER BY p.id DESC")
    List<Long> retornarPedidosNaoEntregues(@Param("clienteId") Long clienteId);

}
