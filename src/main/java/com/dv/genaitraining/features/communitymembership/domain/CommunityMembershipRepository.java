package com.dv.genaitraining.features.communitymembership.domain;

import com.dv.genaitraining.shared.ids.CommunityId;
import com.dv.genaitraining.shared.ids.DancerId;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port for community membership persistence.
 */
public interface CommunityMembershipRepository {
  /**
   * Saves a membership.
   *
   * @param membership membership
   * @return saved membership
   */
  CommunityMembership save(CommunityMembership membership);

  /**
   * Finds an existing membership by dancer and community.
   *
   * @param dancerId dancer id
   * @param communityId community id
   * @return matching membership if present
   */
  Optional<CommunityMembership> findByDancerIdAndCommunityId(DancerId dancerId, CommunityId communityId);

  /**
   * Lists memberships by community.
   *
   * @param communityId community id
   * @return memberships for the community
   */
  List<CommunityMembership> findByCommunityId(CommunityId communityId);

  /**
   * Lists memberships by dancer.
   *
   * @param dancerId dancer id
   * @return memberships for the dancer
   */
  List<CommunityMembership> findByDancerId(DancerId dancerId);
}
