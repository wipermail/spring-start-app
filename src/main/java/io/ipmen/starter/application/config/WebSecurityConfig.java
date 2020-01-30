package io.ipmen.starter.application.config;

import io.ipmen.starter.application.config.security.TokenHelper;
import io.ipmen.starter.application.config.security.auth.RestAuthenticationEntryPoint;
import io.ipmen.starter.application.config.security.auth.TokenAuthenticationFilter;
import io.ipmen.starter.application.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


  private final CustomUserDetailsService jwtUserDetailsService;

  private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

  private final TokenHelper tokenHelper;

  @Autowired
  public WebSecurityConfig(CustomUserDetailsService jwtUserDetailsService,
                           RestAuthenticationEntryPoint restAuthenticationEntryPoint, TokenHelper tokenHelper) {
    this.jwtUserDetailsService = jwtUserDetailsService;
    this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    this.tokenHelper = tokenHelper;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(jwtUserDetailsService)
        .passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
        .authorizeRequests()
        .antMatchers(
            HttpMethod.GET,
            "/",
            "/api/v1/auth/**",
            "/webjars/**",
            "/*.html",
            "/favicon.ico",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js"
        ).permitAll()
        .antMatchers("/api/v1/auth/**").permitAll()
        .anyRequest().authenticated().and()
        .addFilterBefore(
            new TokenAuthenticationFilter(tokenHelper, jwtUserDetailsService), BasicAuthenticationFilter.class);

    http.csrf().disable();
  }


  @Override
  public void configure(WebSecurity web) throws Exception {
    // TokenAuthenticationFilter will ignore the below paths
    web.ignoring().antMatchers(
        HttpMethod.POST,
        "/api/v1/auth/login"
    );
    web.ignoring().antMatchers(
        HttpMethod.GET,
        "/",
        "/webjars/**",
        "/*.html",
        "/favicon.ico",
        "/**/*.html",
        "/**/*.css",
        "/**/*.js"
    );

  }
}
