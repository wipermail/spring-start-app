

package io.ipmen.starter.application.validator.annotation;

import io.ipmen.starter.application.validator.NotCyrillicValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Documented
@Constraint(validatedBy = {NotCyrillicValidator.class})
public @interface ValidateNotCyrillic {
  String message() default "exception.not_cyrillic.wrong";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
