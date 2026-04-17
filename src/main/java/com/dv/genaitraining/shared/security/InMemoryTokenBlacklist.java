package com.dv.genaitraining.shared.security;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory token blacklist (MVP). Intended to be replaced by persistent store later.
 */
@Component
public class InMemoryTokenBlacklist implements TokenBlacklist {
  private final Clock clock;
  private final Map<String, Long> revokedUntilExpEpochSeconds = new ConcurrentHashMap<>();

  public InMemoryTokenBlacklist(Clock clock) {
    this.clock = Objects.requireNonNull(clock, "clock");
  }

  @Override
  public void revoke(String jti, long expEpochSeconds) {
    Objects.requireNonNull(jti, "jti");
    cleanupExpired();
    revokedUntilExpEpochSeconds.put(jti, expEpochSeconds);
  }

  @Override
  public boolean isRevoked(String jti) {
    if (jti == null || jti.isBlank()) return false;
    cleanupExpired();
    Long exp = revokedUntilExpEpochSeconds.get(jti);
    if (exp == null) return false;
    long now = Instant.now(clock).getEpochSecond();
    return now < exp;
  }

  private void cleanupExpired() {
    long now = Instant.now(clock).getEpochSecond();
    revokedUntilExpEpochSeconds.entrySet().removeIf(e -> e.getValue() == null || now >= e.getValue());
  }
}

