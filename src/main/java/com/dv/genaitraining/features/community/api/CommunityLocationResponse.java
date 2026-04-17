package com.dv.genaitraining.features.community.api;

/**
 * Location response payload.
 *
 * @param country optional country
 * @param region optional region
 * @param city optional city
 */
public record CommunityLocationResponse(
    CommunityCountryResponse country,
    String region,
    String city
) {}
