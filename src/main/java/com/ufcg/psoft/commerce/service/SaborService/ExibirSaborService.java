package com.ufcg.psoft.commerce.service.SaborService;

import com.ufcg.psoft.commerce.model.Sabor;

import java.util.List;

@FunctionalInterface
public interface ExibirSaborService {


    List<Sabor> listar(Long idSabor, Long idEstabelecimento, String codigoAcessoEstabelecimento);
}