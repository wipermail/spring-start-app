

package io.ipmen.starter.application.exception;

public class BadRequestException extends RuntimeException {
  public BadRequestException() {
  }

  public BadRequestException(final String message) {
    super(message);
  }
}
