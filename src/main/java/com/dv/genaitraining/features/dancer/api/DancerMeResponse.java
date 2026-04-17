package com.dv.genaitraining.features.dancer.api;

import java.util.UUID;

/**
 * Response payload for the currently logged-in member's dancer profile.
 *
 * @param id dancer id
 * @param name dancer name
 * @param dancerRoles dancer roles
 */
public record DancerMeResponse(
    UUID id,
    String name,
    java.util.List<String> dancerRoles
) {}

