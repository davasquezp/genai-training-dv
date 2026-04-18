package com.dv.genaitraining.features.community.api;

import com.dv.genaitraining.features.community.application.CreateCommunityUseCase;
import com.dv.genaitraining.features.community.domain.Community;
import com.dv.genaitraining.features.community.domain.CommunityCountry;
import com.dv.genaitraining.features.community.domain.CommunityLocation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * Write-only REST adapter for communities.
 */
@RestController
@RequestMapping("/api/communities")
public class CommunityCommandController {
  private final CreateCommunityUseCase create;

  public CommunityCommandController(CreateCommunityUseCase create) {
    this.create = create;
  }

  @PostMapping
  public ResponseEntity<CommunityResponse> create(@Valid @RequestBody CreateCommunityRequest request) {
    Community created = create.create(
        request.name(),
        request.description(),
        request.imageDataUrl(),
        request.global(),
        toDomainLocation(request.location())
    );
    return ResponseEntity.created(URI.create("/api/communities/" + created.id().value())).body(toResponse(created));
  }

  private static CommunityLocation toDomainLocation(CommunityLocationRequest request) {
    if (request == null) return null;
    CommunityCountry country = request.country() == null
        ? null
        : new CommunityCountry(request.country().code(), request.country().name());
    return new CommunityLocation(country, request.region(), request.city());
  }

  private static CommunityResponse toResponse(Community c) {
    return new CommunityResponse(
        c.id().value(),
        c.name(),
        c.description(),
        c.imageDataUrl(),
        c.global(),
        toLocationResponse(c.location()),
        c.createdAt()
    );
  }

  private static CommunityLocationResponse toLocationResponse(CommunityLocation location) {
    if (location == null) return null;
    CommunityCountry country = location.country();
    CommunityCountryResponse countryResponse = country == null ? null : new CommunityCountryResponse(country.code(), country.name());
    return new CommunityLocationResponse(countryResponse, location.region(), location.city());
  }
}
