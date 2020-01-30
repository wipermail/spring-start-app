

package io.ipmen.starter.application.dto.user;

import io.ipmen.starter.application.dto.Dto;
import io.ipmen.starter.application.validator.annotation.ValidatePhone;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class SimpleUserCreateDto extends Dto {
  @NotBlank
  @ValidatePhone
  private String phone;
  @Size(max = 100)
  private String name;
  @NotBlank
  @Size(min = 4, max = 15)
  private String password;
}
