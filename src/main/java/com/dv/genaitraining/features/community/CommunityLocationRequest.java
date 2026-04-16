package com.dv.genaitraining.features.community;

import jakarta.validation.Valid;

/**
 * Location request payload.
 *
 * @param country optional country
 * @param region optional region
 * @param city optional city
 */
public record CommunityLocationRequest(
    @Valid CommunityCountryRequest country,
    String region,
    String city
) {}
