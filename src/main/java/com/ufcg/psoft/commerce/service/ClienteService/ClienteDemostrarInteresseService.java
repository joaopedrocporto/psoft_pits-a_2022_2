package com.ufcg.psoft.commerce.service.ClienteService;

import com.ufcg.psoft.commerce.model.Sabor;
@FunctionalInterface
public interface ClienteDemostrarInteresseService {

    public Sabor interessar(Long idCliente, String codigoAcesso, Long idSabor);
}
