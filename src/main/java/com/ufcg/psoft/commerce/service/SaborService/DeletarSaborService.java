package com.ufcg.psoft.commerce.service.SaborService;

import com.ufcg.psoft.commerce.model.Sabor;

public interface DeletarSaborService {
    void deletar(Long idSabor, Long idEstabelecimento, String codigoAcessoEstabelecimento);
}