package com.dv.genaitraining.features.member;

import jakarta.validation.constraints.NotNull;

/**
 * Request payload for adding a role to the currently logged-in member.
 *
 * @param role role to add
 */
public record AddRoleRequest(@NotNull MemberRole role) {}

