package io.ipmen.starter.application.model;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {

  @Getter
  @Setter
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Getter
  @Setter
  @Column(name = "username", unique = true)
  private String username;

  @Getter
  @Column(name = "password")
  private String password;

  @Getter
  @Setter
  @Column(name = "first_name")
  private String firstName;

  @Getter
  @Setter
  @Column(name = "last_name")
  private String lastName;

  @Getter
  @Setter
  @Column(name = "email", unique = true)
  private String email;

  @Getter
  @Setter
  @Column(name = "phone_number", unique = true)
  private String phoneNumber;

  @Getter
  @Setter
  @Column(name = "enabled")
  private boolean enabled;

  @Getter
  @Setter
  @Column(name = "last_password_reset_date")
  private Timestamp lastPasswordResetDate;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(name = "user_authority",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
  private List<Authority> authorities;

  public void setPassword(String password) {
    Timestamp now = new Timestamp(DateTime.now().getMillis());
    this.setLastPasswordResetDate(now);
    this.password = password;
  }

  public void setAuthorities(List<Authority> authorities) {
    this.authorities = authorities;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

}
