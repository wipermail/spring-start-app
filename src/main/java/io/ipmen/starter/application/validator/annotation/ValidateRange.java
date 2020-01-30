

package io.ipmen.starter.application.validator.annotation;


import io.ipmen.starter.application.validator.RangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Constraint(validatedBy = {RangeValidator.class})
public @interface ValidateRange {
  String message() default "exception.range.wrong";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  double max();

  double min();
}
