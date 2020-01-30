

package io.ipmen.starter.application.validator;

import io.ipmen.starter.application.config.MessageSourceConfiguration;
import io.ipmen.starter.application.validator.annotation.ValidateNotCyrillic;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotCyrillicValidator implements ConstraintValidator<ValidateNotCyrillic, String> {
  private static final String REGEX = ".*\\p{InCyrillic}.*";
  @Autowired
  private MessageSourceConfiguration message;

  public boolean isValid(final String value, final ConstraintValidatorContext context) {
    if (value == null || !value.matches(".*\\p{InCyrillic}.*")) {
      return true;
    }
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(this.message.getMessage(context.getDefaultConstraintMessageTemplate())).addConstraintViolation();
    return false;
  }
}
