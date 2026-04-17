package com.dv.genaitraining.shared.events;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Event bus wiring.
 */
@Configuration
public class EventBusConfig {
  @Bean
  public EventBus eventBus() {
    return new AfterCommitEventBus(new InMemoryEventBus());
  }
}

