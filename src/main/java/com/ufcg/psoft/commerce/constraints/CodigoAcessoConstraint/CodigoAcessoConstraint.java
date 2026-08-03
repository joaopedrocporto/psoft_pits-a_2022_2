package com.ufcg.psoft.commerce.constraints.CodigoAcessoConstraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValidadorCodigoAcesso.class)
public @interface CodigoAcessoConstraint {
    String message() default "Codigo de acesso obrigatorio";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
