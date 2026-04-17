package com.dv.genaitraining.features.communitymembership.api;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request to associate a dancer with a community.
 *
 * @param dancerId dancer id
 * @param communityId community id
 */
public record AssociateCommunityMembershipRequest(
    @NotNull UUID dancerId,
    @NotNull UUID communityId
) {}
