package io.ipmen.starter.application.service.impl;

import io.ipmen.starter.application.dto.user.*;
import io.ipmen.starter.application.service.UserService;
import io.ipmen.starter.application.dto.user.*;
import io.ipmen.starter.application.model.User;
import io.ipmen.starter.application.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

  private final ModelMapper modelMapper;

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final AuthenticationManager authenticationManager;

  private final CustomUserDetailsService customUserDetailsService;

  public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService) {
    this.modelMapper = modelMapper;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.customUserDetailsService = customUserDetailsService;
  }

  @Override
  public User findByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username).orElse(null);
    if (user == null)
      throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));

    return user;
  }

  public User findById(Long id) throws AccessDeniedException {
    return userRepository.findById(id).orElse(null);
  }

  public List<User> findAll() throws AccessDeniedException {
    return userRepository.findAll();
  }

  @Override
  public UserDetailDto convertToDtoDetail(@NotNull User user) {
    return modelMapper.map(user, UserDetailDto.class);
  }

  @Override
  public UserFullDetailDto convertToDtoFullDetail(@NotNull User user) {
    return modelMapper.map(user, UserFullDetailDto.class);
  }

  @Override
  public User convertToModelUser(@NotNull UserCreateDto userCreateDto) {
    return modelMapper.map(userCreateDto, User.class);
  }

  @Override
  public User convertToModelManager(@NotNull ManagerCreateDto managerCreateDto) {
    return modelMapper.map(managerCreateDto, User.class);
  }

  @Override
  public User convertToModelAdmin(@NotNull AdminCreateDto adminCreateDto) {
    return modelMapper.map(adminCreateDto, User.class);
  }

  public boolean changePassword(String oldPassword, String newPassword) {

    Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
    String username = currentUser.getName();

    if (authenticationManager != null) {
      log.debug("Re-authenticating user '" + username + "' for password change request.");
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
    } else {
      log.debug("No authentication manager set. can't change Password!");
      return false;
    }
    log.debug("Changing password for user '" + username + "'");
    User user = (User) this.customUserDetailsService.loadUserByUsername(username);
    user.setPassword(passwordEncoder.encode(newPassword));
    return userRepository.save(user) != null;
  }
}
