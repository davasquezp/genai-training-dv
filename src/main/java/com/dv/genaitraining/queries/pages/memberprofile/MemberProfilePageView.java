package com.dv.genaitraining.queries.pages.memberprofile;

import com.dv.genaitraining.features.member.domain.MemberRole;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record MemberProfilePageView(
    Member member,
    Dancer dancer
) {
  public record Member(
      UUID id,
      String email,
      String name,
      String phone,
      String countryCode,
      String countryName,
      String nationalityCode,
      String nationalityName,
      Set<MemberRole> roles,
      Instant createdAt
  ) {}

  public record Dancer(
      UUID id,
      String name,
      List<String> dancerRoles
  ) {}
}

