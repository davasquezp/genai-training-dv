package com.dv.genaitraining.features.dancer;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Dancer interest registration.
 *
 * @param id dancer id
 * @param name dancer name
 * @param role lead/follower
 * @param countryCode ISO-ish country code
 * @param countryName country name
 * @param styles dance styles (multi-select)
 * @param createdAt timestamp created
 */
public record Dancer(
    DancerId id,
    String name,
    Role role,
    String countryCode,
    String countryName,
    List<DanceStyle> styles,
    Instant createdAt
) {
  /**
   * Creates a dancer.
   *
   * @param id id
   * @param name name
   * @param role role
   * @param countryCode country code
   * @param countryName country name
   * @param styles styles
   * @param createdAt created time
   */
  public Dancer {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(role, "role");
    Objects.requireNonNull(countryCode, "countryCode");
    Objects.requireNonNull(countryName, "countryName");
    Objects.requireNonNull(styles, "styles");
    Objects.requireNonNull(createdAt, "createdAt");
    if (name.isBlank()) {
      throw new IllegalArgumentException("name must not be blank");
    }
    if (countryCode.isBlank()) {
      throw new IllegalArgumentException("countryCode must not be blank");
    }
    if (countryName.isBlank()) {
      throw new IllegalArgumentException("countryName must not be blank");
    }
    if (styles.isEmpty()) {
      throw new IllegalArgumentException("styles must not be empty");
    }
  }
}

