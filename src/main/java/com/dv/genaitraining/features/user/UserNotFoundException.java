package com.dv.genaitraining.features.user;

/**
 * Thrown when a user cannot be found.
 */
public class UserNotFoundException extends RuntimeException {
  /**
   * Creates an exception with a message.
   *
   * @param message message
   */
  public UserNotFoundException(String message) {
    super(message);
  }
}

