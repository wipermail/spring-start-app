

package io.ipmen.starter.application.exception;

public class UnauthorizedException extends RuntimeException {
  public UnauthorizedException() {
  }

  public UnauthorizedException(final String message) {
    super(message);
  }
}
