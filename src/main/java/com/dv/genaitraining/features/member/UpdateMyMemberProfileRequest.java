package com.dv.genaitraining.features.member;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for updating the currently logged-in member's profile.
 *
 * <p>All fields are optional. When a nested payload is present, both fields must be non-blank.</p>
 *
 * @param name member display name
 * @param country country payload (optional)
 * @param nationality nationality payload (optional)
 */
public record UpdateMyMemberProfileRequest(
    @NotBlank String name,
    @Valid CountryPayload country,
    @Valid NationalityPayload nationality
) {
  /**
   * Nested payload for country.
   *
   * @param code code
   * @param name name
   */
  public record CountryPayload(
      @NotBlank String code,
      @NotBlank String name
  ) {}

  /**
   * Nested payload for nationality.
   *
   * @param code code
   * @param name name
   */
  public record NationalityPayload(
      @NotBlank String code,
      @NotBlank String name
  ) {}
}

