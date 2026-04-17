package com.dv.genaitraining.shared.events.member;

import com.dv.genaitraining.features.member.MemberRole;
import com.dv.genaitraining.shared.events.DomainEvent;
import com.dv.genaitraining.shared.ids.MemberId;

import java.util.Objects;
import java.util.Set;

/**
 * Emitted after a member has signed up.
 *
 * @param memberId member id
 * @param roles roles assigned to the member
 */
public record MemberSignedUp(MemberId memberId, Set<MemberRole> roles) implements DomainEvent {
  public MemberSignedUp {
    Objects.requireNonNull(memberId, "memberId");
    Objects.requireNonNull(roles, "roles");
  }
}

