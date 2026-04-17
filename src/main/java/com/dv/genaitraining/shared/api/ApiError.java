package com.dv.genaitraining.shared.api;

import java.time.Instant;

/**
 * Standard API error payload.
 *
 * @param timestamp error time
 * @param status HTTP status code
 * @param code machine-readable code
 * @param message human-readable message
 */
public record ApiError(
    Instant timestamp,
    int status,
    String code,
    String message
) {}

