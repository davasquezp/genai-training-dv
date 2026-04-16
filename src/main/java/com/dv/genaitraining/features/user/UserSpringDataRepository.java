package com.dv.genaitraining.features.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data repository for {@link UserJpaEntity}.
 */
public interface UserSpringDataRepository extends JpaRepository<UserJpaEntity, UUID> {
  /**
   * Finds a user by email.
   *
   * @param email email
   * @return entity if found
   */
  Optional<UserJpaEntity> findByEmail(String email);
}

