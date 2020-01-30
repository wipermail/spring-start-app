package io.ipmen.starter.application.controller;

import io.ipmen.starter.application.config.security.TokenHelper;
import io.ipmen.starter.application.config.security.auth.JwtAuthenticationRequest;
import io.ipmen.starter.application.service.UserService;
import io.ipmen.starter.application.config.MessageSourceConfiguration;
import io.ipmen.starter.application.exception.BadRequestException;
import io.ipmen.starter.application.model.User;
import io.ipmen.starter.application.model.UserTokenState;
import io.ipmen.starter.application.service.impl.CustomUserDetailsService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

  private final TokenHelper tokenHelper;

  @Lazy
  private final AuthenticationManager authenticationManager;

  private final CustomUserDetailsService userDetailsService;

  private final UserService userService;

  private final MessageSourceConfiguration message;

  public AuthenticationController(TokenHelper tokenHelper, AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, UserService userService, MessageSourceConfiguration message) {
    this.tokenHelper = tokenHelper;
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
    this.userService = userService;
    this.message = message;
  }

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public ResponseEntity<?> createAuthenticationToken(
      @RequestBody JwtAuthenticationRequest authenticationRequest,
      HttpServletResponse response
      ) throws AuthenticationException, IOException {

    // Perform the security
    final Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            authenticationRequest.getUsername(),
            authenticationRequest.getPassword()
        )
    );

    // Inject into security context
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // token creation
    User user = (User) authentication.getPrincipal();
    String jws = tokenHelper.generateToken(user.getUsername());
    int expiresIn = tokenHelper.getExpiredIn();
    // Return the token
    return ResponseEntity.ok(new UserTokenState(jws, (long) expiresIn));
  }

  @RequestMapping(value = "/refresh", method = RequestMethod.POST)
  public ResponseEntity<?> refreshAuthenticationToken(
      HttpServletRequest request,
      HttpServletResponse response,
      Principal principal
  ) {

    String authToken = tokenHelper.getToken(request);

    if (authToken != null && principal != null) {

      // TODO check user password last update
      String refreshedToken = tokenHelper.refreshToken(authToken);
      int expiresIn = tokenHelper.getExpiredIn();

      return ResponseEntity.ok(new UserTokenState(refreshedToken, (long) expiresIn));
    } else {
      UserTokenState userTokenState = new UserTokenState();
      return ResponseEntity.accepted().body(userTokenState);
    }
  }

  @RequestMapping(value = "/change-password", method = RequestMethod.POST)
//  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> changePassword(@RequestBody PasswordChanger passwordChanger) {
    if (userService.changePassword(passwordChanger.oldPassword, passwordChanger.newPassword))
      throw new BadRequestException(this.message.getMessage("change.password.bad.request"));
    Map<String, String> result = new HashMap<>();
    result.put("result", "success");
    return ResponseEntity.accepted().body(result);
  }

  static class PasswordChanger {
    public String oldPassword;
    public String newPassword;
  }
}