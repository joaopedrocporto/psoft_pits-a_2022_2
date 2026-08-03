package com.ufcg.psoft.commerce.constraints.EntregadorConstraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = TipoVeiculoIsValid.class)
public @interface TipoVeiculoConstraint {

    String message() default "Tipo do veiculo deve ser moto ou carro";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
