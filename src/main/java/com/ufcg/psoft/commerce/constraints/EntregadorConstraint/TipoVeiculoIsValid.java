package com.ufcg.psoft.commerce.constraints.EntregadorConstraint;

import com.ufcg.psoft.commerce.util.FuncoesValidacao;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TipoVeiculoIsValid implements ConstraintValidator<TipoVeiculoConstraint, String> {

    @Override
    public boolean isValid(String veiculo, ConstraintValidatorContext context) {
        if (!FuncoesValidacao.isNotEmptyOrNull(veiculo)) return false;
        return (veiculo.equals("moto") || veiculo.equals("carro"));
    }
}
