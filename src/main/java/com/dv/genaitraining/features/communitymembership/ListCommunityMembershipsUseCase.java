package com.dv.genaitraining.features.communitymembership;

import java.util.List;
import java.util.UUID;

/**
 * Read use-cases for memberships.
 */
public interface ListCommunityMembershipsUseCase {
  /**
   * Lists memberships for a community.
   *
   * @param communityId community id
   * @return memberships
   */
  List<CommunityMembership> listByCommunity(UUID communityId);

  /**
   * Lists memberships for a dancer.
   *
   * @param dancerId dancer id
   * @return memberships
   */
  List<CommunityMembership> listByDancer(UUID dancerId);
}
