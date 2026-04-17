package com.dv.genaitraining.features.member.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Signup request payload.
 *
 * @param email email
 * @param name name
 * @param phone phone
 * @param password password
 */
public record SignupRequest(
    @NotBlank @Email String email,
    @NotBlank String name,
    @NotBlank String phone,
    @NotBlank String password
) {}

