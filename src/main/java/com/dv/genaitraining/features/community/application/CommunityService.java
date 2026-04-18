package com.dv.genaitraining.features.community.application;

import com.dv.genaitraining.features.community.domain.Community;
import com.dv.genaitraining.features.community.domain.CommunityCountry;
import com.dv.genaitraining.features.community.domain.CommunityLocation;
import com.dv.genaitraining.features.community.domain.CommunityRepository;
import com.dv.genaitraining.shared.ids.CommunityId;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

/**
 * Application service for communities.
 */
@Service
public class CommunityService implements CreateCommunityUseCase {
  private final CommunityRepository repository;
  private final Clock clock;

  /**
   * Creates the service.
   *
   * @param repository repository
   * @param clock clock
   */
  public CommunityService(CommunityRepository repository, Clock clock) {
    this.repository = Objects.requireNonNull(repository, "repository");
    this.clock = Objects.requireNonNull(clock, "clock");
  }

  @Override
  public Community create(
      String name,
      String description,
      String imageDataUrl,
      boolean global,
      CommunityLocation location
  ) {
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(description, "description");
    Community community = new Community(
        CommunityId.newId(),
        name.trim(),
        description.trim(),
        normalizeImageDataUrl(imageDataUrl),
        global,
        normalizeLocation(location),
        Instant.now(clock)
    );
    return repository.save(community);
  }

  private static String normalizeImageDataUrl(String imageDataUrl) {
    if (imageDataUrl == null) return null;
    String trimmed = imageDataUrl.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }

  private static CommunityLocation normalizeLocation(CommunityLocation location) {
    if (location == null) return null;
    String region = location.region() == null ? null : location.region().trim();
    String city = location.city() == null ? null : location.city().trim();
    CommunityCountry country = location.country();
    if (country == null && (region == null || region.isEmpty()) && (city == null || city.isEmpty())) {
      return null;
    }
    return new CommunityLocation(
        country,
        region == null || region.isEmpty() ? null : region,
        city == null || city.isEmpty() ? null : city
    );
  }
}
