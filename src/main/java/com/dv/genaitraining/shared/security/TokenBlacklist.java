package com.dv.genaitraining.shared.security;

/**
 * Blacklist for invalidated JWTs.
 */
public interface TokenBlacklist {
  /**
   * Marks a token id as revoked until its expiration.
   *
   * @param jti token id
   * @param expEpochSeconds expiration epoch seconds
   */
  void revoke(String jti, long expEpochSeconds);

  /**
   * Checks whether a token id is revoked.
   *
   * @param jti token id
   * @return true if revoked
   */
  boolean isRevoked(String jti);
}

