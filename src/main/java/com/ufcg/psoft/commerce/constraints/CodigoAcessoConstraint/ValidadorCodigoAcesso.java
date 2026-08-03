package com.ufcg.psoft.commerce.constraints.CodigoAcessoConstraint;

import com.ufcg.psoft.commerce.util.UtilCodigoAcesso;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidadorCodigoAcesso implements ConstraintValidator<CodigoAcessoConstraint, String> {
    @Override
    public boolean isValid(String codigoAcesso, ConstraintValidatorContext context) {
        return UtilCodigoAcesso.verificarConteudo(codigoAcesso) &&
                UtilCodigoAcesso.validarTamanho(codigoAcesso) &&
                codigoAcesso.matches("\\d{6}");
    }

}

