package br.com.emendes.yourreviewapi.validation.validator;

import br.com.emendes.yourreviewapi.validation.annotation.CustomNotBlank;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator para a anotação {@link CustomNotBlank}.
 */
public class CustomNotBlankValidator implements ConstraintValidator<CustomNotBlank, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) return true;
    return !value.isBlank() && !value.isEmpty();
  }

}
