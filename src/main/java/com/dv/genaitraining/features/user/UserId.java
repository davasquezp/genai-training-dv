package com.dv.genaitraining.features.user;

import java.util.Objects;
import java.util.UUID;

/**
 * Typed identifier for a {@link User}.
 *
 * @param value raw UUID value
 */
public record UserId(UUID value) {
  /**
   * Creates a {@link UserId}.
   *
   * @param value raw UUID value
   */
  public UserId {
    Objects.requireNonNull(value, "value");
  }

  /**
   * Generates a new random {@link UserId}.
   *
   * @return new id
   */
  public static UserId newId() {
    return new UserId(UUID.randomUUID());
  }
}

