package com.dv.genaitraining.features.user;

/**
 * Inbound port for deleting users.
 */
public interface DeleteUserUseCase {
  /**
   * Deletes a user by id.
   *
   * @param id user id
   */
  void delete(UserId id);
}

