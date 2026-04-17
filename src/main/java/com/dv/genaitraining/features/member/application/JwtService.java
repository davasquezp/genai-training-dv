package com.dv.genaitraining.features.member.application;

import com.dv.genaitraining.features.member.domain.Member;
import com.dv.genaitraining.features.member.domain.MemberRole;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Minimal JWT (HS256) issuer and verifier.
 *
 * <p>This is intentionally small for MVP use cases.</p>
 */
public class JwtService {
  private static final ObjectMapper MAPPER = new ObjectMapper();
  private final byte[] secret;
  private final Clock clock;
  private final long ttlSeconds;

  public JwtService(String secret, Clock clock, long ttlSeconds) {
    Objects.requireNonNull(secret, "secret");
    this.secret = secret.getBytes(StandardCharsets.UTF_8);
    this.clock = Objects.requireNonNull(clock, "clock");
    this.ttlSeconds = ttlSeconds;
    if (secret.isBlank()) {
      throw new IllegalArgumentException("JWT secret must not be blank");
    }
    if (ttlSeconds <= 0) {
      throw new IllegalArgumentException("ttlSeconds must be positive");
    }
  }

  public String issue(Member member) {
    Objects.requireNonNull(member, "member");
    try {
      String headerJson = MAPPER.writeValueAsString(Map.of("alg", "HS256", "typ", "JWT"));
      Instant now = Instant.now(clock);
      Instant exp = now.plusSeconds(ttlSeconds);
      String jti = UUID.randomUUID().toString();

      Map<String, Object> payload = new LinkedHashMap<>();
      payload.put("sub", member.id().value().toString());
      payload.put("jti", jti);
      payload.put("email", member.email());
      payload.put("roles", member.roles().stream().map(Enum::name).toList());
      payload.put("iat", now.getEpochSecond());
      payload.put("exp", exp.getEpochSecond());

      String payloadJson = MAPPER.writeValueAsString(payload);

      String header = base64Url(headerJson.getBytes(StandardCharsets.UTF_8));
      String body = base64Url(payloadJson.getBytes(StandardCharsets.UTF_8));
      String signingInput = header + "." + body;
      String sig = base64Url(hmacSha256(signingInput.getBytes(StandardCharsets.UTF_8)));
      return signingInput + "." + sig;
    } catch (Exception e) {
      throw new IllegalStateException("Could not issue token", e);
    }
  }

  public VerifiedToken verify(String token) {
    Objects.requireNonNull(token, "token");
    String[] parts = token.split("\\.");
    if (parts.length != 3) {
      throw new IllegalArgumentException("Invalid token format");
    }
    String signingInput = parts[0] + "." + parts[1];
    byte[] expected = hmacSha256(signingInput.getBytes(StandardCharsets.UTF_8));
    byte[] provided = base64UrlDecode(parts[2]);
    if (!constantTimeEquals(expected, provided)) {
      throw new IllegalArgumentException("Invalid token signature");
    }

    try {
      byte[] payloadBytes = base64UrlDecode(parts[1]);
      @SuppressWarnings("unchecked")
      Map<String, Object> payload = MAPPER.readValue(payloadBytes, Map.class);

      String sub = String.valueOf(payload.get("sub"));
      long exp = Long.parseLong(String.valueOf(payload.get("exp")));
      Instant now = Instant.now(clock);
      if (now.getEpochSecond() >= exp) {
        throw new IllegalArgumentException("Token expired");
      }

      UUID memberId = UUID.fromString(sub);
      Set<MemberRole> roles = ((java.util.List<?>) payload.getOrDefault("roles", java.util.List.of()))
          .stream()
          .map(String::valueOf)
          .map(MemberRole::valueOf)
          .collect(java.util.stream.Collectors.toSet());

      String jti = String.valueOf(payload.getOrDefault("jti", ""));
      return new VerifiedToken(memberId, roles, jti, exp);
    } catch (IllegalArgumentException e) {
      throw e;
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid token payload", e);
    }
  }

  private byte[] hmacSha256(byte[] data) {
    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(new SecretKeySpec(secret, "HmacSHA256"));
      return mac.doFinal(data);
    } catch (Exception e) {
      throw new IllegalStateException("HMAC failure", e);
    }
  }

  private static String base64Url(byte[] bytes) {
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

  private static byte[] base64UrlDecode(String s) {
    return Base64.getUrlDecoder().decode(s);
  }

  private static boolean constantTimeEquals(byte[] a, byte[] b) {
    if (a.length != b.length) return false;
    int result = 0;
    for (int i = 0; i < a.length; i++) {
      result |= a[i] ^ b[i];
    }
    return result == 0;
  }

  /**
   * Verified token claims we care about.
   *
   * @param memberId member id
   * @param roles roles
   * @param jti token id
   * @param expEpochSeconds expiration time (epoch seconds)
   */
  public record VerifiedToken(UUID memberId, Set<MemberRole> roles, String jti, long expEpochSeconds) {}
}

