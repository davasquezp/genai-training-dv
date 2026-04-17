package com.dv.genaitraining.features.dancer.api;

import com.dv.genaitraining.features.dancer.domain.DanceStyle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Request payload for registering dancer interest.
 *
 * @param name name
 * @param role lead/follower (\"lead\" or \"follower\")
 * @param styles dance styles
 */
public record RegisterDancerRequest(
    @NotBlank @Size(max = 200) String name,
    @NotBlank String role,
    @NotEmpty List<@NotNull DanceStyle> styles
) {}

