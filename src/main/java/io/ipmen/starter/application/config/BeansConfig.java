package io.ipmen.starter.application.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BeansConfig {
  /*mapper for DTO*/
  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
  /*end mapper for DTO*/
}
