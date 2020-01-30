

package io.ipmen.starter.application.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.ipmen.starter.application.model.Role;
import io.ipmen.starter.application.validator.annotation.ValidateEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AdminCreateDto extends SimpleUserCreateDto {
  @NotBlank
  @JsonInclude
  @ValidateEnum(enumeration = Role.class, exclude = {"MANAGER, USER"})
  private String role;
}
