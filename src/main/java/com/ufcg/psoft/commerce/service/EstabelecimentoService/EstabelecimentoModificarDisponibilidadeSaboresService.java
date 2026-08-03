package com.ufcg.psoft.commerce.service.EstabelecimentoService;

@FunctionalInterface
public interface EstabelecimentoModificarDisponibilidadeSaboresService {

    Boolean modificarDisponibilidadeSabor(Long idEstabelecimento, Long idSabor, Boolean disponibilidade);

}
