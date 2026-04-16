package com.dv.genaitraining.features.community;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Application service for communities.
 */
@Service
public class CommunityService implements CreateCommunityUseCase, GetCommunityUseCase, ListCommunitiesUseCase {
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

  @Override
  public Optional<Community> get(UUID id) {
    Objects.requireNonNull(id, "id");
    return repository.findById(new CommunityId(id));
  }

  @Override
  public List<Community> list() {
    return repository.findAll();
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
