package com.dv.genaitraining.features.user;

import java.time.Instant;

/**
 * Error payload returned by the API.
 *
 * @param timestamp error time
 * @param status HTTP status code
 * @param error stable error code
 * @param message human-readable message
 */
public record ApiError(
    Instant timestamp,
    int status,
    String error,
    String message
) {}

