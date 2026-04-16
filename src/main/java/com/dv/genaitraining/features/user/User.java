package com.dv.genaitraining.features.user;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain user aggregate.
 *
 * @param id user identifier
 * @param email unique email address
 * @param displayName human-friendly name
 * @param createdAt creation timestamp
 * @param updatedAt last update timestamp
 */
public record User(
    UserId id,
    String email,
    String displayName,
    Instant createdAt,
    Instant updatedAt
) {
  /**
   * Creates a user domain object.
   *
   * @param id user identifier
   * @param email unique email address
   * @param displayName human-friendly name
   * @param createdAt creation timestamp
   * @param updatedAt last update timestamp
   */
  public User {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(email, "email");
    Objects.requireNonNull(displayName, "displayName");
    Objects.requireNonNull(createdAt, "createdAt");
    Objects.requireNonNull(updatedAt, "updatedAt");
    if (email.isBlank()) {
      throw new IllegalArgumentException("email must not be blank");
    }
    if (displayName.isBlank()) {
      throw new IllegalArgumentException("displayName must not be blank");
    }
  }
}

