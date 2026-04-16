package com.dv.genaitraining.features.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request payload for updating a user.
 *
 * @param email unique email
 * @param displayName display name
 */
public record UpdateUserRequest(
    @NotBlank @Email String email,
    @NotBlank @Size(max = 200) String displayName
) {}

