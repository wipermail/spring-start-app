package io.ipmen.starter.application.controller;

import io.ipmen.starter.application.model.User;
import io.ipmen.starter.application.service.UserService;
import io.ipmen.starter.application.config.MessageSourceConfiguration;
import io.ipmen.starter.application.dto.user.UserDetailDto;
import io.ipmen.starter.application.dto.user.UserFullDetailDto;
import io.ipmen.starter.application.exception.ResourceNotFoundException;
import io.ipmen.starter.application.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/api/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

  private final UserService userService;
  private final MessageSourceConfiguration message;

  @Autowired
  public UserController(UserService userService, MessageSourceConfiguration message) {
    this.userService = userService;
    this.message = message;
  }

  @RequestMapping(method = GET, value = "/all")
  @PreAuthorize("hasAnyAuthority('ADMIN, MANAGER')")
  public @ResponseBody
  List<UserDetailDto> loadAll() {
    List<UserDetailDto> userDetailDtos = new ArrayList<>();
    this.userService.findAll().forEach(user -> userDetailDtos.add(userService.convertToDtoDetail(user)));
    return userDetailDtos;
  }

  @RequestMapping(method = GET, value = "/{userId}")
  @PreAuthorize("hasAnyAuthority('ADMIN, MANAGER')")
  public @ResponseBody
  UserFullDetailDto
  loadById(@PathVariable Long userId) {
    User user = this.userService.findById(userId);
    if (user == null)
      throw new ResourceNotFoundException(this.message.getMessage("user.not_found", new Object[]{userId}));
    return userService.convertToDtoFullDetail(user);
  }


  /*
   *  We are not using userService.findByUsername here(we could),
   *  so it is good that we are making sure that the user has role "ROLE_USER"
   *  to access this endpoint.
   */
  @RequestMapping(value = "/whoami", method = GET)
  @PreAuthorize("hasAnyAuthority('USER, MANAGER, ADMIN')")
  public @ResponseBody
  UserFullDetailDto user(Principal user) {
    User user1 = this.userService.findByUsername(user.getName());
    if (user1 == null)
      throw new UnauthorizedException(this.message.getMessage("unauthorized_exception"));

    return this.userService.convertToDtoFullDetail(user1);
  }
}
