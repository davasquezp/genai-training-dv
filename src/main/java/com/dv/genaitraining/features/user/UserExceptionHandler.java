package com.dv.genaitraining.features.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

/**
 * REST exception mapping for typed feature exceptions.
 */
@RestControllerAdvice
public class UserExceptionHandler {
  /**
   * Maps user-not-found to HTTP 404.
   *
   * @param ex exception
   * @return API error
   */
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ApiError> notFound(UserNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ApiError(Instant.now(), 404, "USER_NOT_FOUND", ex.getMessage()));
  }

  /**
   * Maps uniqueness conflicts to HTTP 409.
   *
   * @param ex exception
   * @return API error
   */
  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ApiError> conflict(UserAlreadyExistsException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ApiError(Instant.now(), 409, "USER_ALREADY_EXISTS", ex.getMessage()));
  }

  /**
   * Maps validation errors to HTTP 400.
   *
   * @param ex exception
   * @return API error
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getFieldErrors().stream()
        .map(UserExceptionHandler::formatFieldError)
        .collect(Collectors.joining("; "));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ApiError(Instant.now(), 400, "VALIDATION_ERROR", message));
  }

  private static String formatFieldError(FieldError fe) {
    String defaultMessage = fe.getDefaultMessage() == null ? "invalid" : fe.getDefaultMessage();
    return fe.getField() + " " + defaultMessage;
  }
}

