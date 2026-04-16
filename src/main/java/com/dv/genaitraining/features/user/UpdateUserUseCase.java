package com.dv.genaitraining.features.user;

/**
 * Inbound port for updating users.
 */
public interface UpdateUserUseCase {
  /**
   * Updates user fields.
   *
   * @param id user id
   * @param email unique email
   * @param displayName display name
   * @return updated user
   */
  User update(UserId id, String email, String displayName);
}

