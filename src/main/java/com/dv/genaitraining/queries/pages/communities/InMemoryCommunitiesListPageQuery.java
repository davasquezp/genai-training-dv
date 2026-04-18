package com.dv.genaitraining.queries.pages.communities;

import com.dv.genaitraining.features.community.domain.Community;
import com.dv.genaitraining.features.community.domain.CommunityCountry;
import com.dv.genaitraining.features.community.domain.CommunityLocation;
import com.dv.genaitraining.features.community.domain.CommunityRepository;
import com.dv.genaitraining.features.communitymembership.domain.CommunityMembershipRepository;
import com.dv.genaitraining.shared.ids.CommunityId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InMemoryCommunitiesListPageQuery implements CommunitiesListPageQuery {
  private final CommunityRepository communityRepository;
  private final CommunityMembershipRepository membershipRepository;

  public InMemoryCommunitiesListPageQuery(
      CommunityRepository communityRepository,
      CommunityMembershipRepository membershipRepository
  ) {
    this.communityRepository = communityRepository;
    this.membershipRepository = membershipRepository;
  }

  @Override
  public CommunitiesListPageView load() {
    List<CommunitiesListPageView.Item> items = communityRepository.findAll().stream()
        .map(c -> new CommunitiesListPageView.Item(
            c.id().value(),
            c.name(),
            c.description(),
            c.imageDataUrl(),
            c.global(),
            toLocation(c.location()),
            c.createdAt(),
            membershipRepository.findByCommunityId(new CommunityId(c.id().value())).size()
        ))
        .toList();
    return new CommunitiesListPageView(items);
  }

  private static CommunitiesListPageView.Location toLocation(CommunityLocation location) {
    if (location == null) return null;
    return new CommunitiesListPageView.Location(
        toCountry(location.country()),
        location.region(),
        location.city()
    );
  }

  private static CommunitiesListPageView.Country toCountry(CommunityCountry country) {
    if (country == null) return null;
    return new CommunitiesListPageView.Country(country.code(), country.name());
  }
}

