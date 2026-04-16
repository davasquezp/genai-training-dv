package com.dv.genaitraining.features.user;

/**
 * Inbound port for creating users.
 */
public interface CreateUserUseCase {
  /**
   * Creates a new user.
   *
   * @param email unique email
   * @param displayName display name
   * @return created user
   */
  User create(String email, String displayName);
}

