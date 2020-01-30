

package io.ipmen.starter.application.exception;

public class PermissionException extends RuntimeException {
  public PermissionException() {
  }

  public PermissionException(final String message) {
    super(message);
  }
}
