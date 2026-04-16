package com.dv.genaitraining.features.user;

/**
 * Thrown when attempting to create or update a user in a way that violates a uniqueness constraint.
 */
public class UserAlreadyExistsException extends RuntimeException {
  /**
   * Creates an exception with a message.
   *
   * @param message message
   */
  public UserAlreadyExistsException(String message) {
    super(message);
  }
}

