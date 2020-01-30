package io.ipmen.starter.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@AllArgsConstructor
public class UserTokenState {
  @Getter
  @Setter
  private String access_token;
  @Getter
  @Setter
  private Long expires_in;

  public UserTokenState() {
    this.access_token = null;
    this.expires_in = null;
  }
}