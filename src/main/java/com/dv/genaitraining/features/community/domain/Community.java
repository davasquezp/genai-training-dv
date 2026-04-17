package com.dv.genaitraining.features.community.domain;

import com.dv.genaitraining.shared.ids.CommunityId;
import java.time.Instant;
import java.util.Objects;

/**
 * Community aggregate.
 *
 * @param id id
 * @param name name
 * @param description description
 * @param imageDataUrl optional image data-url
 * @param global indicates global scope
 * @param location optional location details
 * @param createdAt creation timestamp
 */
public record Community(
    CommunityId id,
    String name,
    String description,
    String imageDataUrl,
    boolean global,
    CommunityLocation location,
    Instant createdAt
) {
  /**
   * Creates a community.
   *
   * @param id id
   * @param name name
   * @param description description
   * @param imageDataUrl image
   * @param global global
   * @param location location
   * @param createdAt created
   */
  public Community {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(description, "description");
    Objects.requireNonNull(createdAt, "createdAt");
    if (name.isBlank()) {
      throw new IllegalArgumentException("name must not be blank");
    }
    if (description.isBlank()) {
      throw new IllegalArgumentException("description must not be blank");
    }
    if (!global && (location == null || !location.hasAnyValue())) {
      throw new IllegalArgumentException("location is required when community is not global");
    }
  }
}
