package com.dv.genaitraining.features.communitymembership.application;

import com.dv.genaitraining.features.community.domain.CommunityRepository;
import com.dv.genaitraining.features.communitymembership.domain.CommunityMembership;
import com.dv.genaitraining.features.communitymembership.domain.CommunityMembershipId;
import com.dv.genaitraining.features.communitymembership.domain.CommunityMembershipRepository;
import com.dv.genaitraining.features.dancer.domain.DancerRepository;
import com.dv.genaitraining.shared.ids.CommunityId;
import com.dv.genaitraining.shared.ids.DancerId;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Application service for community memberships.
 */
@Service
public class CommunityMembershipService implements AssociateDancerToCommunityUseCase, ListCommunityMembershipsUseCase {
  private final CommunityMembershipRepository membershipRepository;
  private final DancerRepository dancerRepository;
  private final CommunityRepository communityRepository;
  private final Clock clock;

  /**
   * Creates the service.
   *
   * @param membershipRepository membership repository
   * @param dancerRepository dancer repository
   * @param communityRepository community repository
   * @param clock clock
   */
  public CommunityMembershipService(
      CommunityMembershipRepository membershipRepository,
      DancerRepository dancerRepository,
      CommunityRepository communityRepository,
      Clock clock
  ) {
    this.membershipRepository = Objects.requireNonNull(membershipRepository, "membershipRepository");
    this.dancerRepository = Objects.requireNonNull(dancerRepository, "dancerRepository");
    this.communityRepository = Objects.requireNonNull(communityRepository, "communityRepository");
    this.clock = Objects.requireNonNull(clock, "clock");
  }

  @Override
  public CommunityMembership associate(UUID dancerIdValue, UUID communityIdValue) {
    Objects.requireNonNull(dancerIdValue, "dancerId");
    Objects.requireNonNull(communityIdValue, "communityId");
    DancerId dancerId = new DancerId(dancerIdValue);
    CommunityId communityId = new CommunityId(communityIdValue);

    if (dancerRepository.findById(dancerId).isEmpty()) {
      throw new IllegalArgumentException("Dancer not found: " + dancerIdValue);
    }
    if (communityRepository.findById(communityId).isEmpty()) {
      throw new IllegalArgumentException("Community not found: " + communityIdValue);
    }

    return membershipRepository.findByDancerIdAndCommunityId(dancerId, communityId)
        .orElseGet(() -> {
          CommunityMembership membership = new CommunityMembership(
              CommunityMembershipId.newId(),
              dancerId,
              communityId,
              Instant.now(clock)
          );
          return membershipRepository.save(membership);
        });
  }

  @Override
  public List<CommunityMembership> listByCommunity(UUID communityIdValue) {
    Objects.requireNonNull(communityIdValue, "communityId");
    return membershipRepository.findByCommunityId(new CommunityId(communityIdValue));
  }

  @Override
  public List<CommunityMembership> listByDancer(UUID dancerIdValue) {
    Objects.requireNonNull(dancerIdValue, "dancerId");
    return membershipRepository.findByDancerId(new DancerId(dancerIdValue));
  }
}
