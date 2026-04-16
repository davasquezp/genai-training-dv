package com.dv.genaitraining.features.communitymembership;

import java.util.Objects;
import java.util.UUID;

/**
 * Typed identifier for {@link CommunityMembership}.
 *
 * @param value raw UUID value
 */
public record CommunityMembershipId(UUID value) {
  /**
   * Creates an id.
   *
   * @param value raw UUID
   */
  public CommunityMembershipId {
    Objects.requireNonNull(value, "value");
  }

  /**
   * Generates a new id.
   *
   * @return new id
   */
  public static CommunityMembershipId newId() {
    return new CommunityMembershipId(UUID.randomUUID());
  }
}
