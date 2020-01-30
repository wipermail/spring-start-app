package io.ipmen.starter.application.config.security.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationRequest {
  @Getter
  @Setter
  private String username;
  @Getter
  @Setter
  private String password;
}
