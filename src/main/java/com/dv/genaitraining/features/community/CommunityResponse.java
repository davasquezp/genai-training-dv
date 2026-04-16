package com.dv.genaitraining.features.community;

import java.time.Instant;
import java.util.UUID;

/**
 * Community response payload.
 *
 * @param id id
 * @param name name
 * @param description description
 * @param imageDataUrl optional image data-url
 * @param global global
 * @param location optional location
 * @param createdAt created at
 */
public record CommunityResponse(
    UUID id,
    String name,
    String description,
    String imageDataUrl,
    boolean global,
    CommunityLocationResponse location,
    Instant createdAt
) {}
