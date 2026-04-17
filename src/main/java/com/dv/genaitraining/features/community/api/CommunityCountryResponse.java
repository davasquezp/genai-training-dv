package com.dv.genaitraining.features.community.api;

/**
 * Country response payload.
 *
 * @param code country code
 * @param name country name
 */
public record CommunityCountryResponse(
    String code,
    String name
) {}
