package com.dv.genaitraining.queries.pages.community;

import java.time.Instant;
import java.util.UUID;

public record CommunityDetailPageView(
    Community community,
    int dancerCount,
    Viewer viewer
) {
  public record Community(
      UUID id,
      String name,
      String description,
      String imageDataUrl,
      boolean global,
      Location location,
      Instant createdAt
  ) {}

  public record Location(
      Country country,
      String region,
      String city
  ) {}

  public record Country(
      String code,
      String name
  ) {}

  public record Viewer(
      boolean authenticated,
      boolean isDancer,
      boolean alreadyMember,
      UUID myDancerId
  ) {}
}

