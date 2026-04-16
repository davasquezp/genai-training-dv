package com.dv.genaitraining.features.dancer;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Response payload representing a dancer registration.
 *
 * @param id id
 * @param name name
 * @param role role
 * @param countryCode country code
 * @param countryName country name
 * @param styles styles
 * @param createdAt created at
 */
public record DancerResponse(
    UUID id,
    String name,
    String role,
    String countryCode,
    String countryName,
    List<String> styles,
    Instant createdAt
) {}

