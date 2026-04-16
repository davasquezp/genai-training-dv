package com.dv.genaitraining.features.community;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * REST adapter for communities.
 */
@RestController
@RequestMapping("/api/communities")
public class CommunityController {
  private final CreateCommunityUseCase create;
  private final ListCommunitiesUseCase list;
  private final GetCommunityUseCase get;

  /**
   * Creates the controller.
   *
   * @param create create use-case
   * @param list list use-case
   * @param get get use-case
   */
  public CommunityController(CreateCommunityUseCase create, ListCommunitiesUseCase list, GetCommunityUseCase get) {
    this.create = create;
    this.list = list;
    this.get = get;
  }

  /**
   * Creates a new community.
   *
   * @param request request payload
   * @return created community
   */
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

  /**
   * Lists communities.
   *
   * @return communities
   */
  @GetMapping
  public List<CommunityResponse> list() {
    return list.list().stream().map(CommunityController::toResponse).toList();
  }

  /**
   * Gets one community.
   *
   * @param id community id
   * @return community
   */
  @GetMapping("/{id}")
  public ResponseEntity<CommunityResponse> get(@PathVariable UUID id) {
    return get.get(id)
        .map(CommunityController::toResponse)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
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
