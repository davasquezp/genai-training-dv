package com.dv.genaitraining.features.user;

import java.util.List;

/**
 * Inbound port for reading users.
 */
public interface GetUserUseCase {
  /**
   * Fetches a user by id.
   *
   * @param id user id
   * @return user
   */
  User getById(UserId id);

  /**
   * Lists all users.
   *
   * @return users
   */
  List<User> list();
}

