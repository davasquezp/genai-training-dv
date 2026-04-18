package com.dv.genaitraining.shared;

import com.dv.genaitraining.features.member.application.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Cross-cutting JWT configuration.
 */
@Configuration
public class JwtConfig {
  @Bean
  public JwtService jwtService(
      Clock clock,
      @Value("${APP_JWT_SECRET:}") String secret,
      @Value("${APP_JWT_TTL_SECONDS:604800}") long ttlSeconds
  ) {
    return new JwtService(secret, clock, ttlSeconds);
  }
}

