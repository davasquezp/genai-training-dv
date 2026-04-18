package com.dv.genaitraining.queries.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Cross-cutting error handling for page-shaped query endpoints.
 */
@RestControllerAdvice(basePackages = "com.dv.genaitraining.queries")
public class PageQueryExceptionHandler {
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> badRequest(IllegalArgumentException e) {
    String msg = e.getMessage() == null ? "Bad request" : e.getMessage();
    return ResponseEntity.badRequest().body(msg);
  }
}

