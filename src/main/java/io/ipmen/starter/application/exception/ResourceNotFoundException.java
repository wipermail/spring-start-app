

package io.ipmen.starter.application.exception;

public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException() {
  }

  public ResourceNotFoundException(final String message) {
    super(message);
  }
}
