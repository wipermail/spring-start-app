package io.ipmen.starter.application.config.security.auth;

import io.ipmen.starter.application.config.security.TokenHelper;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class TokenAuthenticationFilter extends OncePerRequestFilter {

  private TokenHelper tokenHelper;

  private UserDetailsService userDetailsService;

  public TokenAuthenticationFilter(TokenHelper tokenHelper, UserDetailsService userDetailsService) {
    this.tokenHelper = tokenHelper;
    this.userDetailsService = userDetailsService;
  }


  @Override
  public void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain
  ) throws IOException, ServletException {

    String token = tokenHelper.getToken(request);

    if (token != null) {
      // get username from token
      String username = tokenHelper.getUsernameFromToken(token);
      if (username != null) {
        // get user
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (tokenHelper.validateToken(token, userDetails)) {
          // create authentication
          TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
          authentication.setToken(token);
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    }
    chain.doFilter(request, response);
  }

}