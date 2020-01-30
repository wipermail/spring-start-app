package io.ipmen.starter.application.init;

import io.ipmen.starter.application.model.Authority;
import io.ipmen.starter.application.model.Role;
import io.ipmen.starter.application.model.User;
import io.ipmen.starter.application.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
  private final ApplicationContext context;
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder encoder;

  @Autowired
  public CommandLineAppStartupRunner(ApplicationContext context, UserRepository userRepository) {
    this.context = context;
    this.userRepository = userRepository;
    encoder = new BCryptPasswordEncoder();
    ;
  }

  @Override
  public void run(String... args) throws Exception {
    final Environment env = context.getEnvironment();
    createClient();
    createAdmin();
    createManager();

  }

  private void createClient() {
    if (!userRepository.existsByUsername("79667714332")) {
      User user = new User();
      user.setPassword(this.encoder.encode("1234"));
      user.setEnabled(true);
      user.setUsername("79667714332");
      List<Authority> authorities = new ArrayList<>();
      Authority userRole = new Authority();
      userRole.setName(Role.USER);
      authorities.add(userRole);
      user.setAuthorities(authorities);
      userRepository.save(user);
    }
  }

  private void createAdmin() {
    if (!userRepository.existsByUsername("79667714333")) {
      User user = new User();
      user.setPassword(this.encoder.encode("1234"));
      user.setEnabled(true);
      user.setUsername("79667714333");
      List<Authority> authorities = new ArrayList<>();
      Authority admin = new Authority();
      Authority userRole = new Authority();
      userRole.setName(Role.USER);
      authorities.add(userRole);
      Authority adminRole = new Authority();
      adminRole.setName(Role.ADMIN);
      authorities.add(adminRole);
      Authority managerRole = new Authority();
      managerRole.setName(Role.MANAGER);
      authorities.add(managerRole);
      user.setAuthorities(authorities);
      userRepository.save(user);
    }
  }

  private void createManager() {
    if (!userRepository.existsByUsername("79999999999")) {
      User user = new User();
      user.setPassword(this.encoder.encode("1234"));
      user.setEnabled(true);
      user.setUsername("79999999999");
      List<Authority> authorities = new ArrayList<>();
      Authority managerRole = new Authority();
      managerRole.setName(Role.MANAGER);
      authorities.add(managerRole);
      user.setAuthorities(authorities);
      userRepository.save(user);
    }
  }
}
