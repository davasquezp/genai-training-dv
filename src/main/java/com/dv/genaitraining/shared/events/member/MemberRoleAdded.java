package com.dv.genaitraining.shared.events.member;

import com.dv.genaitraining.features.member.domain.MemberRole;
import com.dv.genaitraining.shared.events.DomainEvent;
import com.dv.genaitraining.shared.ids.MemberId;

import java.util.Objects;

/**
 * Emitted after a role has been added to a member.
 *
 * @param memberId member id
 * @param role role that was added
 */
public record MemberRoleAdded(MemberId memberId, MemberRole role) implements DomainEvent {
  public MemberRoleAdded {
    Objects.requireNonNull(memberId, "memberId");
    Objects.requireNonNull(role, "role");
  }
}

