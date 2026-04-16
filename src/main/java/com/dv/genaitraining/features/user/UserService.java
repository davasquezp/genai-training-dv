package com.dv.genaitraining.features.user;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Application service implementing the User CRUD use-cases.
 */
@Service
public class UserService implements CreateUserUseCase, GetUserUseCase, UpdateUserUseCase, DeleteUserUseCase {
  private final UserRepository userRepository;
  private final Clock clock;

  /**
   * Creates a {@link UserService}.
   *
   * @param userRepository persistence port
   * @param clock clock used for timestamps
   */
  public UserService(UserRepository userRepository, Clock clock) {
    this.userRepository = Objects.requireNonNull(userRepository, "userRepository");
    this.clock = Objects.requireNonNull(clock, "clock");
  }

  @Override
  public User create(String email, String displayName) {
    Objects.requireNonNull(email, "email");
    Objects.requireNonNull(displayName, "displayName");

    userRepository.findByEmail(email).ifPresent(u -> {
      throw new UserAlreadyExistsException("User with email already exists: " + email);
    });

    Instant now = Instant.now(clock);
    User user = new User(UserId.newId(), email, displayName, now, now);
    return userRepository.save(user);
  }

  @Override
  public User getById(UserId id) {
    Objects.requireNonNull(id, "id");
    return userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found: " + id.value()));
  }

  @Override
  public List<User> list() {
    return userRepository.findAll();
  }

  @Override
  public User update(UserId id, String email, String displayName) {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(email, "email");
    Objects.requireNonNull(displayName, "displayName");

    User existing = getById(id);

    userRepository.findByEmail(email)
        .filter(u -> !u.id().equals(id))
        .ifPresent(u -> {
          throw new UserAlreadyExistsException("User with email already exists: " + email);
        });

    Instant now = Instant.now(clock);
    User updated = new User(existing.id(), email, displayName, existing.createdAt(), now);
    return userRepository.save(updated);
  }

  @Override
  public void delete(UserId id) {
    Objects.requireNonNull(id, "id");
    boolean deleted = userRepository.deleteById(id);
    if (!deleted) {
      throw new UserNotFoundException("User not found: " + id.value());
    }
  }
}

