

package io.ipmen.starter.application.validator;

import io.ipmen.starter.application.config.MessageSourceConfiguration;
import io.ipmen.starter.application.validator.annotation.ValidateRange;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RangeValidator implements ConstraintValidator<ValidateRange, Double> {
  @Autowired
  private MessageSourceConfiguration message;
  private double max;
  private double min;

  public void initialize(final ValidateRange constraintAnnotation) {
    this.max = constraintAnnotation.max();
    this.min = constraintAnnotation.min();
  }

  public boolean isValid(final Double value, final ConstraintValidatorContext context) {
    if (value >= this.min && value <= this.max) {
      return true;
    }
    context.disableDefaultConstraintViolation();
    final String msg = this.message.getMessage(context.getDefaultConstraintMessageTemplate(), new Object[]{this.min, this.max});
    context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    return false;
  }
}
