package com.dv.genaitraining.features.member;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Password hashing using PBKDF2 with per-user salt.
 */
public class PasswordHasher {
  private static final int SALT_BYTES = 16;
  private static final int ITERATIONS = 120_000;
  private static final int KEY_LENGTH_BITS = 256;
  private final SecureRandom random = new SecureRandom();

  public HashedPassword hash(char[] password) {
    byte[] salt = new byte[SALT_BYTES];
    random.nextBytes(salt);
    byte[] hash = pbkdf2(password, salt);
    return new HashedPassword(
        Base64.getEncoder().encodeToString(hash),
        Base64.getEncoder().encodeToString(salt)
    );
  }

  public boolean verify(char[] password, String expectedHashBase64, String saltBase64) {
    byte[] salt = Base64.getDecoder().decode(saltBase64);
    byte[] computed = pbkdf2(password, salt);
    byte[] expected = Base64.getDecoder().decode(expectedHashBase64);
    return constantTimeEquals(expected, computed);
  }

  private static byte[] pbkdf2(char[] password, byte[] salt) {
    try {
      PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH_BITS);
      SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      return skf.generateSecret(spec).getEncoded();
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new IllegalStateException("Password hashing failed", e);
    }
  }

  private static boolean constantTimeEquals(byte[] a, byte[] b) {
    if (a.length != b.length) return false;
    int result = 0;
    for (int i = 0; i < a.length; i++) {
      result |= a[i] ^ b[i];
    }
    return result == 0;
  }

  public record HashedPassword(String hashBase64, String saltBase64) {}
}