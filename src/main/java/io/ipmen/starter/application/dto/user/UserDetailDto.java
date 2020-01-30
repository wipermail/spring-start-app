package io.ipmen.starter.application.dto.user;

import io.ipmen.starter.application.dto.Dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDto extends Dto {

  @Getter
  @Setter
  private Long id;

  @Getter
  @Setter
  private String username;

  @Getter
  @Setter
  private String email;

  @Getter
  @Setter
  private String phoneNumber;

  @Getter
  @Setter
  private boolean enabled;
}
