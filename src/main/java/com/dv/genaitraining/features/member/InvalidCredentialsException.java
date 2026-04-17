package com.dv.genaitraining.features.member;

/**
 * Thrown when credentials are invalid.
 */
public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException() {
    super("Invalid credentials");
  }
}

