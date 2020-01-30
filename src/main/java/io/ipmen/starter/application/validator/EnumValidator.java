

package io.ipmen.starter.application.validator;

import io.ipmen.starter.application.config.MessageSourceConfiguration;
import io.ipmen.starter.application.validator.annotation.ValidateEnum;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<ValidateEnum, String> {
  @Autowired
  private MessageSourceConfiguration message;
  private List<String> list;
  private boolean ignoreCase;

  public void initialize(final ValidateEnum constraintAnnotation) {
    if (constraintAnnotation.enumeration().isEnum()) {
      this.list = Arrays.stream(constraintAnnotation.enumeration().getEnumConstants()).map(Object::toString).collect(Collectors.toList());
      this.list.removeAll(Arrays.asList(constraintAnnotation.exclude()));
      this.list.addAll(new ArrayList(Arrays.asList(constraintAnnotation.include())));
      this.ignoreCase = constraintAnnotation.ignoreCase();
      if (this.ignoreCase) {
        this.list.replaceAll(String::toLowerCase);
      }
    }
  }

  public boolean isValid(final String value, final ConstraintValidatorContext context) {
    if (this.list == null) {
      return false;
    }
    if (value == null) {
      return true;
    }
    if (this.ignoreCase) {
      if (this.list.stream().anyMatch((x) -> x.equalsIgnoreCase(value))) {
        return true;
      }
    } else if (this.list.stream().anyMatch((x) -> x.equals(value))) {
      return true;
    }
    context.disableDefaultConstraintViolation();
    final String msg = this.message.getMessage(context.getDefaultConstraintMessageTemplate(), new Object[]{String.join(", ", this.list)});
    context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    return false;
  }
}
