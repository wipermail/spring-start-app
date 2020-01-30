package io.ipmen.starter.application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "authority")
public class Authority implements GrantedAuthority {

  @Getter
  @Setter
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private Long id;

  @Getter
  @Setter
  @Enumerated(EnumType.STRING)
  @Column(name = "name")
  @JsonIgnore
  private Role name;

  @Override
  public String getAuthority() {
    return name.name();
  }
}
