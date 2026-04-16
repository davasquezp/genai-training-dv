package com.dv.genaitraining.features.community;

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
