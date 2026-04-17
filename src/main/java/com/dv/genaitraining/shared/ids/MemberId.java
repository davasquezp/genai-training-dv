package com.dv.genaitraining.shared.ids;

import java.util.Objects;
import java.util.UUID;

/**
 * Cross-cutting typed identifier for a Member.
 *
 * @param value raw UUID
 */
public record MemberId(UUID value) {
  public MemberId {
    Objects.requireNonNull(value, "value");
  }

  public static MemberId newId() {
    return new MemberId(UUID.randomUUID());
  }
}

