package io.ipmen.starter.application.dto.user;

import io.ipmen.starter.application.model.Authority;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserFullDetailDto extends UserDetailDto {

  @Getter
  @Setter
  private String firstName;

  @Getter
  @Setter
  private String lastName;

  @Getter
  @Setter
  private List<Authority> authorities;
}
