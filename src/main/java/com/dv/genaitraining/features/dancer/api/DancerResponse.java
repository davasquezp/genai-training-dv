package com.dv.genaitraining.features.dancer.api;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Response payload representing a dancer registration.
 *
 * @param id id
 * @param name name
 * @param roles roles
 * @param styles styles
 * @param createdAt created at
 */
public record DancerResponse(
    UUID id,
    String name,
    List<String> roles,
    List<String> styles,
    Instant createdAt
) {}

