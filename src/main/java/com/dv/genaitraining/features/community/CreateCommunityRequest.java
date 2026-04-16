package com.dv.genaitraining.features.community;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for creating a community.
 *
 * @param name name
 * @param description description
 * @param imageDataUrl optional image data-url
 * @param global global flag
 * @param location optional location
 */
public record CreateCommunityRequest(
    @NotBlank String name,
    @NotBlank String description,
    String imageDataUrl,
    boolean global,
    @Valid CommunityLocationRequest location
) {}
