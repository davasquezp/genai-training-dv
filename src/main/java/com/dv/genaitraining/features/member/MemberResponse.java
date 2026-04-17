package com.dv.genaitraining.features.member;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/**
 * Member response payload.
 *
 * @param id id
 * @param email email
 * @param name optional name
 * @param phone phone
 * @param countryCode optional country code
 * @param countryName optional country name
 * @param nationalityCode optional nationality code
 * @param nationalityName optional nationality name
 * @param roles roles
 * @param createdAt created at
 */
public record MemberResponse(
    UUID id,
    String email,
    String name,
    String phone,
    String countryCode,
    String countryName,
    String nationalityCode,
    String nationalityName,
    Set<MemberRole> roles,
    Instant createdAt
) {}

