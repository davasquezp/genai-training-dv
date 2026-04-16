package com.dv.genaitraining.features.user;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * JPA-backed adapter implementing {@link UserRepository}.
 */
@Repository
public class UserJpaAdapter implements UserRepository {
  private final UserSpringDataRepository springDataRepository;

  /**
   * Creates the adapter.
   *
   * @param springDataRepository spring data repository
   */
  public UserJpaAdapter(UserSpringDataRepository springDataRepository) {
    this.springDataRepository = Objects.requireNonNull(springDataRepository, "springDataRepository");
  }

  @Override
  public Optional<User> findById(UserId id) {
    return springDataRepository.findById(id.value()).map(UserJpaAdapter::toDomain);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return springDataRepository.findByEmail(email).map(UserJpaAdapter::toDomain);
  }

  @Override
  public List<User> findAll() {
    return springDataRepository.findAll().stream().map(UserJpaAdapter::toDomain).toList();
  }

  @Override
  public User save(User user) {
    UserJpaEntity saved = springDataRepository.save(toEntity(user));
    return toDomain(saved);
  }

  @Override
  public boolean deleteById(UserId id) {
    if (!springDataRepository.existsById(id.value())) {
      return false;
    }
    springDataRepository.deleteById(id.value());
    return true;
  }

  private static User toDomain(UserJpaEntity e) {
    return new User(
        new UserId(e.getId()),
        e.getEmail(),
        e.getDisplayName(),
        e.getCreatedAt(),
        e.getUpdatedAt()
    );
  }

  private static UserJpaEntity toEntity(User u) {
    return new UserJpaEntity(
        u.id().value(),
        u.email(),
        u.displayName(),
        u.createdAt(),
        u.updatedAt()
    );
  }
}

