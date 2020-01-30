package io.ipmen.starter.application.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Configuration
public class MessageSourceConfiguration {
  private final MessageSource messageSource;

  public String getMessage(final String id, final Object[] args) {
    final Locale currentLocale = LocaleContextHolder.getLocale();
    return this.messageSource.getMessage(id, args, currentLocale);
  }

  public String getMessage(final String id) {
    return this.getMessage(id, null);
  }

  public MessageSourceConfiguration(final MessageSource messageSource) {
    this.messageSource = messageSource;
  }
}
