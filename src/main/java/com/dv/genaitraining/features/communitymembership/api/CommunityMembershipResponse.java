package com.dv.genaitraining.features.communitymembership.api;

import java.time.Instant;
import java.util.UUID;

/**
 * Response payload for a community membership.
 *
 * @param id membership id
 * @param dancerId dancer id
 * @param communityId community id
 * @param createdAt creation timestamp
 */
public record CommunityMembershipResponse(
    UUID id,
    UUID dancerId,
    UUID communityId,
    Instant createdAt
) {}
