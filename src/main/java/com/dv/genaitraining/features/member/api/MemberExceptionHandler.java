package com.dv.genaitraining.features.member.api;

import com.dv.genaitraining.features.member.domain.InvalidCredentialsException;
import com.dv.genaitraining.features.member.domain.MemberEmailAlreadyExistsException;
import com.dv.genaitraining.features.member.domain.MemberPhoneAlreadyExistsException;
import com.dv.genaitraining.shared.api.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

/**
 * REST exception mapping for member feature.
 */
@RestControllerAdvice
public class MemberExceptionHandler {
  @ExceptionHandler(MemberEmailAlreadyExistsException.class)
  public ResponseEntity<ApiError> emailConflict(MemberEmailAlreadyExistsException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ApiError(Instant.now(), 409, "MEMBER_EMAIL_ALREADY_EXISTS", ex.getMessage()));
  }

  @ExceptionHandler(MemberPhoneAlreadyExistsException.class)
  public ResponseEntity<ApiError> phoneConflict(MemberPhoneAlreadyExistsException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ApiError(Instant.now(), 409, "MEMBER_PHONE_ALREADY_EXISTS", ex.getMessage()));
  }

  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<ApiError> invalidCredentials(InvalidCredentialsException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new ApiError(Instant.now(), 401, "INVALID_CREDENTIALS", ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getFieldErrors().stream()
        .map(fe -> fe.getField() + " " + (fe.getDefaultMessage() == null ? "invalid" : fe.getDefaultMessage()))
        .collect(Collectors.joining("; "));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ApiError(Instant.now(), 400, "VALIDATION_ERROR", message));
  }
}

