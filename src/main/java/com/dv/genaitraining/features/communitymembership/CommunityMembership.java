package com.dv.genaitraining.features.communitymembership;

import com.dv.genaitraining.features.community.CommunityId;
import com.dv.genaitraining.features.dancer.DancerId;

import java.time.Instant;
import java.util.Objects;

/**
 * Relationship entity that represents a dancer belonging to a community.
 *
 * @param id membership id
 * @param dancerId dancer id
 * @param communityId community id
 * @param createdAt association timestamp
 */
public record CommunityMembership(
    CommunityMembershipId id,
    DancerId dancerId,
    CommunityId communityId,
    Instant createdAt
) {
  /**
   * Creates a membership.
   *
   * @param id membership id
   * @param dancerId dancer id
   * @param communityId community id
   * @param createdAt association timestamp
   */
  public CommunityMembership {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(dancerId, "dancerId");
    Objects.requireNonNull(communityId, "communityId");
    Objects.requireNonNull(createdAt, "createdAt");
  }
}
