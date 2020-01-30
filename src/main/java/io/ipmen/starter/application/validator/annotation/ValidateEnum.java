

package io.ipmen.starter.application.validator.annotation;

import io.ipmen.starter.application.validator.EnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Documented
@Constraint(validatedBy = {EnumValidator.class})
public @interface ValidateEnum {
  String message() default "exception.enum.wrong";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  Class<?> enumeration();

  String[] exclude() default {};

  String[] include() default {};

  boolean ignoreCase() default false;
}
