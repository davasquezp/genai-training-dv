package com.dv.genaitraining.features.dancer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request payload for updating the currently logged-in member's dancer profile.
 *
 * @param name dancer name
 * @param dancerRoles dancer roles (optional, defaults to empty)
 */
public record UpdateMyDancerRequest(
    @NotBlank @Size(max = 200) String name,
    java.util.List<Role> dancerRoles
) {
}

