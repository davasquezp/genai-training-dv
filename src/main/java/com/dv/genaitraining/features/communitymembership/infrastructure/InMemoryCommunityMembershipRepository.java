package com.dv.genaitraining.features.communitymembership.infrastructure;

import com.dv.genaitraining.features.communitymembership.domain.CommunityMembership;
import com.dv.genaitraining.features.communitymembership.domain.CommunityMembershipRepository;
import com.dv.genaitraining.shared.ids.CommunityId;
import com.dv.genaitraining.shared.ids.DancerId;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory adapter for memberships.
 */
@Repository
public class InMemoryCommunityMembershipRepository implements CommunityMembershipRepository {
  private final Map<java.util.UUID, CommunityMembership> byId = new ConcurrentHashMap<>();

  @Override
  public CommunityMembership save(CommunityMembership membership) {
    byId.put(membership.id().value(), membership);
    return membership;
  }

  @Override
  public Optional<CommunityMembership> findByDancerIdAndCommunityId(DancerId dancerId, CommunityId communityId) {
    return byId.values().stream()
        .filter(m -> m.dancerId().equals(dancerId) && m.communityId().equals(communityId))
        .findFirst();
  }

  @Override
  public List<CommunityMembership> findByCommunityId(CommunityId communityId) {
    return byId.values().stream()
        .filter(m -> m.communityId().equals(communityId))
        .sorted(Comparator.comparing(CommunityMembership::createdAt).reversed())
        .toList();
  }

  @Override
  public List<CommunityMembership> findByDancerId(DancerId dancerId) {
    return byId.values().stream()
        .filter(m -> m.dancerId().equals(dancerId))
        .sorted(Comparator.comparing(CommunityMembership::createdAt).reversed())
        .toList();
  }
}
