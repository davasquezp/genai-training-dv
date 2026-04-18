package com.dv.genaitraining.queries.pages.communities;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CommunitiesListPageView(
    List<Item> items
) {
  public record Item(
      UUID id,
      String name,
      String description,
      String imageDataUrl,
      boolean global,
      Location location,
      Instant createdAt,
      int dancerCount
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
}

