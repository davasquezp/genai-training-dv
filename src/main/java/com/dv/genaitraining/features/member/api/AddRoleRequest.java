package com.dv.genaitraining.features.member.api;

import com.dv.genaitraining.features.member.domain.MemberRole;
import jakarta.validation.constraints.NotNull;

/**
 * Request payload for adding a role to the currently logged-in member.
 *
 * @param role role to add
 */
public record AddRoleRequest(@NotNull MemberRole role) {}

