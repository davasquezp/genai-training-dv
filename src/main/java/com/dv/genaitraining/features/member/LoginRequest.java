package com.dv.genaitraining.features.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Login request payload.
 *
 * @param email email
 * @param password password
 */
public record LoginRequest(
    @NotBlank @Email String email,
    @NotBlank String password
) {}

