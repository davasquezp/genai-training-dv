package com.dv.genaitraining.features.dancer;

import com.dv.genaitraining.shared.ids.MemberId;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Dancer interest registration.
 *
 * @param id dancer id
 * @param memberId optional member id (when dancer is created from a member account)
 * @param name dancer name
 * @param roles dance roles (lead/follower), multi-select
 * @param styles dance styles (multi-select)
 * @param createdAt timestamp created
 */
public record Dancer(
    DancerId id,
    MemberId memberId,
    String name,
    List<Role> roles,
    List<DanceStyle> styles,
    Instant createdAt
) {
  /**
   * Creates a dancer.
   *
   * @param id id
   * @param name name
   * @param role role
   * @param styles styles
   * @param createdAt created time
   */
  public Dancer {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(roles, "roles");
    Objects.requireNonNull(styles, "styles");
    Objects.requireNonNull(createdAt, "createdAt");
  }
}

