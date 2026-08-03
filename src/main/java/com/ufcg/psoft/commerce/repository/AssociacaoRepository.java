package com.ufcg.psoft.commerce.repository;

import com.ufcg.psoft.commerce.model.Associacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssociacaoRepository extends JpaRepository<Associacao, Long> {

    @Query("SELECT a.id FROM Associacao a WHERE a.entregadorId = :entregadorId")
    List<Long> findAllIdsByEntregadorId(@Param("entregadorId") Long entregadorId);

    @Query("SELECT a FROM Associacao a WHERE a.entregadorId = :entregadorId")
    List<Associacao> retornarAssociacoes(@Param("entregadorId") Long entregadorId);

    @Query(
            "SELECT a FROM Associacao a WHERE a.entregadorId = :entregadorId and a.estabelecimentoId = :estabelecimentoId"
    )
    Associacao retornarAssociacao(
            @Param("entregadorId") Long entregadorId,
            @Param("estabelecimentoId") Long estabelecimentoId
            );

    @Query("SELECT a.id FROM Associacao a WHERE a.estabelecimentoId = :estabelecimentoId")
    List<Long> findAllIdsByEstabelecimentoId(@Param("estabelecimentoId") Long estabelecimentoId);

    @Query("SELECT a.estabelecimentoId FROM Associacao a WHERE a.entregadorId  = :entregadorId")
    List<Long> retornarEstabelecimentosComAssociacaoEntregador(@Param("entregadorId") Long entregadorId);
}
