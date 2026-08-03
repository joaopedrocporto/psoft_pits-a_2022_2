package com.ufcg.psoft.commerce.service.EstabelecimentoService;

import com.ufcg.psoft.commerce.model.Estabelecimento;

import java.util.List;

@FunctionalInterface
public interface EstabelecimentoListarService {
    public List<Estabelecimento> get(Long id);
}