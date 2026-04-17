package com.dv.genaitraining.features.member.api;

/**
 * Auth response payload.
 *
 * @param token JWT token
 * @param member member summary
 */
public record AuthResponse(String token, MemberResponse member) {}

