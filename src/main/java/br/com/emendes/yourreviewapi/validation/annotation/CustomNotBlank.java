package br.com.emendes.yourreviewapi.validation.annotation;

import br.com.emendes.yourreviewapi.validation.validator.CustomNotBlankValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * O elemento anotado não deve estar em branco ou ser vazio.
 * <p>
 * Tipos suportados:
 * <ul>
 *     <li>{@code String}</li>
 * </ul>
 * <p>
 * Elementos {@code null} são considerados válidos.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomNotBlankValidator.class)
public @interface CustomNotBlank {

  String message() default "must not be empty or blank";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
