package com.dv.genaitraining.features.community;

import java.util.Objects;

/**
 * Country data for a community location.
 *
 * @param code country code
 * @param name country name
 */
public record CommunityCountry(String code, String name) {
  /**
   * Creates a country value object.
   *
   * @param code code
   * @param name name
   */
  public CommunityCountry {
    Objects.requireNonNull(code, "code");
    Objects.requireNonNull(name, "name");
    if (code.isBlank()) {
      throw new IllegalArgumentException("code must not be blank");
    }
    if (name.isBlank()) {
      throw new IllegalArgumentException("name must not be blank");
    }
  }
}
