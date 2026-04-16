package com.dv.genaitraining.features.user;

import java.time.Instant;
import java.util.UUID;

/**
 * Response payload representing a user.
 *
 * @param id user id
 * @param email email
 * @param displayName display name
 * @param createdAt creation timestamp
 * @param updatedAt last update timestamp
 */
public record UserResponse(
    UUID id,
    String email,
    String displayName,
    Instant createdAt,
    Instant updatedAt
) {}

