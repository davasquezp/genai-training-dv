package com.dv.genaitraining.shared.ids;

import java.util.Objects;
import java.util.UUID;

/**
 * Typed identifier for a {@link Dancer}.
 *
 * @param value raw UUID value
 */
public record DancerId(UUID value) {
  /**
   * Creates a {@link DancerId}.
   *
   * @param value raw UUID value
   */
  public DancerId {
    Objects.requireNonNull(value, "value");
  }

  /**
   * Generates a new random {@link DancerId}.
   *
   * @return new id
   */
  public static DancerId newId() {
    return new DancerId(UUID.randomUUID());
  }
}

