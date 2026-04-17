package com.dv.genaitraining.features.dancer.domain;

import com.dv.genaitraining.features.member.domain.MemberRole;
import com.dv.genaitraining.shared.events.EventBus;
import com.dv.genaitraining.shared.events.EventHandler;
import com.dv.genaitraining.shared.events.dancer.DancerCreated;
import com.dv.genaitraining.shared.events.member.MemberRoleAdded;
import com.dv.genaitraining.shared.ids.DancerId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Creates a dancer when a member becomes a dancer.
 */
@Component
public class MemberRoleAddedHandler implements EventHandler<MemberRoleAdded> {
  private final DancerRepository dancerRepository;
  private final EventBus eventBus;
  private final Clock clock;

  public MemberRoleAddedHandler(DancerRepository dancerRepository, EventBus eventBus, Clock clock) {
    this.dancerRepository = Objects.requireNonNull(dancerRepository, "dancerRepository");
    this.eventBus = Objects.requireNonNull(eventBus, "eventBus");
    this.clock = Objects.requireNonNull(clock, "clock");
  }

  @Override
  @Transactional
  public void handle(MemberRoleAdded event) {
    Objects.requireNonNull(event, "event");
    if (event.role() != MemberRole.DANCER) {
      return;
    }

    if (dancerRepository.findByMemberId(event.memberId()).isPresent()) {
      return;
    }

    Instant now = Instant.now(clock);
    Dancer created = new Dancer(
        DancerId.newId(),
        event.memberId(),
        "",
        List.of(),
        List.of(),
        now
    );
    dancerRepository.save(created);
    eventBus.publish(new DancerCreated(created.id().value(), event.memberId()));
  }
}

