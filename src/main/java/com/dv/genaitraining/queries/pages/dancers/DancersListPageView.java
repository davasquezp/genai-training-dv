package com.dv.genaitraining.queries.pages.dancers;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record DancersListPageView(
    List<Item> items
) {
  public record Item(
      UUID id,
      String name,
      List<String> roles,
      List<String> styles,
      Instant createdAt
  ) {}
}

