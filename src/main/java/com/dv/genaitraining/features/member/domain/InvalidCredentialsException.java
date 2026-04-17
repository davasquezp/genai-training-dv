package com.dv.genaitraining.features.member.domain;

/**
 * Thrown when credentials are invalid.
 */
public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException() {
    super("Invalid credentials");
  }
}

