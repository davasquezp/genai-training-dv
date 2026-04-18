package com.dv.genaitraining.queries.pages.community;

import com.dv.genaitraining.features.community.domain.Community;
import com.dv.genaitraining.features.community.domain.CommunityCountry;
import com.dv.genaitraining.features.community.domain.CommunityLocation;
import com.dv.genaitraining.features.community.domain.CommunityRepository;
import com.dv.genaitraining.features.communitymembership.domain.CommunityMembership;
import com.dv.genaitraining.features.communitymembership.domain.CommunityMembershipRepository;
import com.dv.genaitraining.features.dancer.domain.DancerRepository;
import com.dv.genaitraining.shared.ids.CommunityId;
import com.dv.genaitraining.shared.ids.MemberId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InMemoryCommunityDetailPageQuery implements CommunityDetailPageQuery {
  private final CommunityRepository communityRepository;
  private final CommunityMembershipRepository membershipRepository;
  private final DancerRepository dancerRepository;

  public InMemoryCommunityDetailPageQuery(
      CommunityRepository communityRepository,
      CommunityMembershipRepository membershipRepository,
      DancerRepository dancerRepository
  ) {
    this.communityRepository = communityRepository;
    this.membershipRepository = membershipRepository;
    this.dancerRepository = dancerRepository;
  }

  @Override
  public CommunityDetailPageView load(UUID communityIdValue, org.springframework.security.core.Authentication authentication) {
    if (communityIdValue == null) throw new IllegalArgumentException("communityId is required");
    CommunityId communityId = new CommunityId(communityIdValue);

    Community community = communityRepository.findById(communityId).orElse(null);
    if (community == null) {
      throw new IllegalArgumentException("Community not found: " + communityIdValue);
    }

    List<CommunityMembership> memberships = membershipRepository.findByCommunityId(communityId);
    int dancerCount = memberships.size();

    ViewerState viewerState = resolveViewer(authentication, memberships);

    return new CommunityDetailPageView(
        toCommunity(community),
        dancerCount,
        viewerState.viewer()
    );
  }

  private ViewerState resolveViewer(
      org.springframework.security.core.Authentication authentication,
      List<CommunityMembership> memberships
  ) {
    if (authentication == null || authentication.getPrincipal() == null) {
      return new ViewerState(new CommunityDetailPageView.Viewer(false, false, false, null));
    }

    UUID memberUuid;
    try {
      memberUuid = UUID.fromString(String.valueOf(authentication.getPrincipal()));
    } catch (Exception e) {
      return new ViewerState(new CommunityDetailPageView.Viewer(true, false, false, null));
    }

    var dancer = dancerRepository.findByMemberId(new MemberId(memberUuid)).orElse(null);
    if (dancer == null) {
      return new ViewerState(new CommunityDetailPageView.Viewer(true, false, false, null));
    }

    UUID myDancerId = dancer.id().value();
    boolean alreadyMember = memberships.stream()
        .map(m -> m.dancerId().value())
        .anyMatch(id -> id.equals(myDancerId));

    return new ViewerState(new CommunityDetailPageView.Viewer(true, true, alreadyMember, myDancerId));
  }

  private record ViewerState(CommunityDetailPageView.Viewer viewer) {}

  private static CommunityDetailPageView.Community toCommunity(Community c) {
    return new CommunityDetailPageView.Community(
        c.id().value(),
        c.name(),
        c.description(),
        c.imageDataUrl(),
        c.global(),
        toLocation(c.location()),
        c.createdAt()
    );
  }

  private static CommunityDetailPageView.Location toLocation(CommunityLocation location) {
    if (location == null) return null;
    return new CommunityDetailPageView.Location(
        toCountry(location.country()),
        location.region(),
        location.city()
    );
  }

  private static CommunityDetailPageView.Country toCountry(CommunityCountry country) {
    if (country == null) return null;
    return new CommunityDetailPageView.Country(country.code(), country.name());
  }
}

