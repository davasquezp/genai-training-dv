package com.dv.genaitraining.features.user;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port for user persistence.
 */
public interface UserRepository {
  /**
   * Looks up a user by id.
   *
   * @param id user id
   * @return user if present
   */
  Optional<User> findById(UserId id);

  /**
   * Looks up a user by email.
   *
   * @param email email
   * @return user if present
   */
  Optional<User> findByEmail(String email);

  /**
   * Lists all users.
   *
   * @return users
   */
  List<User> findAll();

  /**
   * Saves a user (create or update).
   *
   * @param user user to persist
   * @return persisted user
   */
  User save(User user);

  /**
   * Deletes a user by id.
   *
   * @param id user id
   * @return true if a row was deleted
   */
  boolean deleteById(UserId id);
}

