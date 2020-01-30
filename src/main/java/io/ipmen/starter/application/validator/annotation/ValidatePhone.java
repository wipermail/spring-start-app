

package io.ipmen.starter.application.validator.annotation;

import io.ipmen.starter.application.validator.PhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Constraint(validatedBy = {PhoneValidator.class})
public @interface ValidatePhone {
  String message() default "exception.phone.wrong";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
