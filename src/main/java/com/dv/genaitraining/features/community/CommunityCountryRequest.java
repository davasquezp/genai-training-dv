package com.dv.genaitraining.features.community;

import jakarta.validation.constraints.NotBlank;

/**
 * Country request payload.
 *
 * @param code country code
 * @param name country name
 */
public record CommunityCountryRequest(
    @NotBlank String code,
    @NotBlank String name
) {}
