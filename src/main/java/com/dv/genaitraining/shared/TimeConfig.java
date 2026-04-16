package com.dv.genaitraining.shared;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Cross-cutting time configuration.
 */
@Configuration
public class TimeConfig {
  /**
   * Provides the system clock.
   *
   * @return clock
   */
  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }
}

