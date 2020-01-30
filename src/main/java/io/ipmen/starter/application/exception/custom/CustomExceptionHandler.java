package io.ipmen.starter.application.exception.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import io.ipmen.starter.application.config.MessageSourceConfiguration;
import io.ipmen.starter.application.exception.BadRequestException;
import io.ipmen.starter.application.exception.PermissionException;
import io.ipmen.starter.application.exception.ResourceNotFoundException;
import io.ipmen.starter.application.exception.UnauthorizedException;
import io.ipmen.starter.application.pojo.CustomMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolationException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {
  private final MessageSourceConfiguration message;
  private final String sizeMax;

  @Autowired
  public CustomExceptionHandler(final MessageSourceConfiguration message, @Value("${spring.servlet.multipart.max-file-size}") final String sizeMax) {
    this.message = message;
    this.sizeMax = sizeMax;
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler({UnauthorizedException.class})
  protected CustomMessage handleUnauthorizedException(final UnauthorizedException ex) {
    return new CustomMessage(ex.getMessage());
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler({PermissionException.class})
  protected CustomMessage handleForbiddenException(final PermissionException ex) {
    return new CustomMessage(ex.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({BadRequestException.class})
  protected CustomMessage handleBadrequestException(final BadRequestException ex) {
    return new CustomMessage(ex.getMessage());
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({ResourceNotFoundException.class})
  protected CustomMessage handleNotFoundException(final ResourceNotFoundException ex) {
    return new CustomMessage(ex.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({MethodArgumentNotValidException.class})
  protected CustomMessage handleValidationException(final MethodArgumentNotValidException ex) {
    final BindingResult result = ex.getBindingResult();
    final FieldError error = result.getFieldError();
    try {
      final String msg = this.message.getMessage(error.getDefaultMessage());
      return new CustomMessage(msg);
    } catch (NoSuchMessageException e) {
      String fieldName = error.getField();
      try {
        final Field field = result.getTarget().getClass().getDeclaredField(fieldName);
        if (field.isAnnotationPresent((Class<? extends Annotation>) JsonProperty.class)) {
          fieldName = field.getAnnotation(JsonProperty.class).value();
        }
      } catch (NoSuchFieldException ex2) {
      }
      return new CustomMessage(fieldName + " - " + error.getDefaultMessage());
    }
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ConstraintViolationException.class})
  protected CustomMessage handleViolationException(final ConstraintViolationException ex) {
    String message = (String) ex.getConstraintViolations().stream().map((x) -> {
      String property = x.getPropertyPath().toString();
      if (property.indexOf(".") != -1) {
        property = property.substring(property.indexOf(".") + 1);
      }

      return property + " - " + x.getMessage();
    }).collect(Collectors.joining("\n"));
    return new CustomMessage(message);
  }

  @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
  @ExceptionHandler({MaxUploadSizeExceededException.class})
  protected CustomMessage handleMaxUploadSizeException(final MaxUploadSizeExceededException ex) {
    return new CustomMessage(this.message.getMessage("exception.max.upload.size", new Object[]{this.sizeMax}));
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({JsonParseException.class})
  protected CustomMessage handleJsonParseException(final JsonParseException ex) {
    return new CustomMessage(ex.getMessage());
  }
}
