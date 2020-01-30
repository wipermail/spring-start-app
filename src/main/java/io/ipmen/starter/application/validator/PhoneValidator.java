

package io.ipmen.starter.application.validator;

import io.ipmen.starter.application.validator.annotation.ValidatePhone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidatePhone, String> {
  public boolean isValid(final String value, final ConstraintValidatorContext context) {
    if (value == null || value.matches("^[0-9]{11,12}$")) {
      return true;
    }
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addConstraintViolation();
    return false;
  }
}
