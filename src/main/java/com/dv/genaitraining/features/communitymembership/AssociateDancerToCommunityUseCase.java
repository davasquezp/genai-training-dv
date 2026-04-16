package com.dv.genaitraining.features.communitymembership;

import java.util.UUID;

/**
 * Use-case for associating a dancer to a community.
 */
public interface AssociateDancerToCommunityUseCase {
  /**
   * Creates a new association between dancer and community.
   *
   * @param dancerId dancer id
   * @param communityId community id
   * @return created or existing membership
   */
  CommunityMembership associate(UUID dancerId, UUID communityId);
}
