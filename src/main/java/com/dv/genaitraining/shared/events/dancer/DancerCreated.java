package com.dv.genaitraining.shared.events.dancer;

import com.dv.genaitraining.shared.events.DomainEvent;
import com.dv.genaitraining.shared.ids.MemberId;

import java.util.Objects;
import java.util.UUID;

/**
 * Emitted after a dancer entity has been created.
 *
 * @param dancerId dancer id
 * @param memberId member id
 */
public record DancerCreated(UUID dancerId, MemberId memberId) implements DomainEvent {
  public DancerCreated {
    Objects.requireNonNull(dancerId, "dancerId");
    Objects.requireNonNull(memberId, "memberId");
  }
}

