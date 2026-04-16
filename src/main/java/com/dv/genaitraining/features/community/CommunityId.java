package com.dv.genaitraining.features.community;

import java.util.Objects;
import java.util.UUID;

/**
 * Typed identifier for {@link Community}.
 *
 * @param value raw UUID value
 */
public record CommunityId(UUID value) {
  /**
   * Creates a community id.
   *
   * @param value raw UUID value
   */
  public CommunityId {
    Objects.requireNonNull(value, "value");
  }

  /**
   * Generates a new community id.
   *
   * @return new id
   */
  public static CommunityId newId() {
    return new CommunityId(UUID.randomUUID());
  }
}
