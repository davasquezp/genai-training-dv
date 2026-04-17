package com.dv.genaitraining.features.member;

import com.dv.genaitraining.shared.ids.MemberId;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

/**
 * Member account.
 *
 * @param id id
 * @param email unique email
 * @param name optional display name
 * @param phone unique phone (required)
 * @param passwordHash password hash
 * @param passwordSalt salt used for hashing
 * @param countryCode optional country code
 * @param countryName optional country name
 * @param nationalityCode optional nationality code
 * @param nationalityName optional nationality name
 * @param roles roles (multi-select)
 * @param createdAt created at
 */
public record Member(
    MemberId id,
    String email,
    String name,
    String phone,
    String passwordHash,
    String passwordSalt,
    String countryCode,
    String countryName,
    String nationalityCode,
    String nationalityName,
    Set<MemberRole> roles,
    Instant createdAt
) {
  /**
   * Creates a member.
   *
   * @param id id
   * @param email email
   * @param phone phone
   * @param passwordHash password hash
   * @param passwordSalt salt
   * @param roles roles
   * @param createdAt created at
   */
  public Member {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(email, "email");
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(phone, "phone");
    Objects.requireNonNull(passwordHash, "passwordHash");
    Objects.requireNonNull(passwordSalt, "passwordSalt");
    Objects.requireNonNull(roles, "roles");
    Objects.requireNonNull(createdAt, "createdAt");

    if (email.isBlank()) {
      throw new IllegalArgumentException("email must not be blank");
    }
    if (name.isBlank()) {
      throw new IllegalArgumentException("name must not be blank");
    }
    if (phone.isBlank()) {
      throw new IllegalArgumentException("phone must not be blank");
    }
    if (passwordHash.isBlank()) {
      throw new IllegalArgumentException("passwordHash must not be blank");
    }
    if (passwordSalt.isBlank()) {
      throw new IllegalArgumentException("passwordSalt must not be blank");
    }
    if (roles.isEmpty()) {
      throw new IllegalArgumentException("roles must not be empty");
    }
  }
}

