package com.dv.genaitraining.features.dancer;

import com.dv.genaitraining.shared.events.EventBus;
import com.dv.genaitraining.shared.events.member.MemberRoleAdded;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.Objects;

/**
 * Dancer bounded-context event wiring.
 */
@Configuration
public class DancerEventsConfig {
  private final EventBus eventBus;
  private final MemberRoleAddedHandler handler;

  public DancerEventsConfig(EventBus eventBus, MemberRoleAddedHandler handler) {
    this.eventBus = Objects.requireNonNull(eventBus, "eventBus");
    this.handler = Objects.requireNonNull(handler, "handler");
  }

  @PostConstruct
  public void subscribeHandlers() {
    eventBus.subscribe(MemberRoleAdded.class, handler);
  }
}

