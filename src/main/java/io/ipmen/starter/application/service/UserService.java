package io.ipmen.starter.application.service;


import io.ipmen.starter.application.dto.user.*;
import io.ipmen.starter.application.dto.user.*;
import io.ipmen.starter.application.model.User;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface UserService {
  User findById(Long id);

  User findByUsername(String username);

  List<User> findAll();

  UserDetailDto convertToDtoDetail(@NotNull User user);

  UserFullDetailDto convertToDtoFullDetail(@NotNull User user);

  User convertToModelUser(@NotNull UserCreateDto userCreateDto);

  User convertToModelManager(@NotNull ManagerCreateDto managerCreateDto);

  User convertToModelAdmin(@NotNull AdminCreateDto adminCreateDto);

  boolean changePassword(@NotNull String oldPassword, @NotNull String newPassword);
}
