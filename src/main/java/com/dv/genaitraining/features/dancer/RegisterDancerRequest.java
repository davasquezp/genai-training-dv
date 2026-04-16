package com.dv.genaitraining.features.dancer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Request payload for registering dancer interest.
 *
 * @param name name
 * @param role lead/follower (\"lead\" or \"follower\")
 * @param country country payload
 * @param styles dance styles
 */
public record RegisterDancerRequest(
    @NotBlank @Size(max = 200) String name,
    @NotBlank String role,
    @NotNull @Valid CountryPayload country,
    @NotEmpty List<@NotNull DanceStyle> styles
) {
  /**
   * Nested payload for country.
   *
   * @param code country code
   * @param name country name
   */
  public record CountryPayload(
      @NotBlank String code,
      @NotBlank String name
  ) {}
}

