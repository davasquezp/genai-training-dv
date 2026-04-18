package com.dv.genaitraining.queries.pages.dancer;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record DancerDetailPageView(
    Dancer dancer
) {
  public record Dancer(
      UUID id,
      String name,
      List<String> roles,
      List<String> styles,
      Instant createdAt
  ) {}
}

