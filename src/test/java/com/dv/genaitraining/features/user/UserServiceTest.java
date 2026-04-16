package com.dv.genaitraining.features.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");
  private static final Clock CLOCK = Clock.fixed(NOW, ZoneOffset.UTC);

  @Mock UserRepository userRepository;
  @Captor ArgumentCaptor<User> userCaptor;

  @Test
  void create_savesNewUserWithTimestamps() {
    when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.empty());
    when(userRepository.save(userCaptor.capture())).thenAnswer(inv -> inv.getArgument(0));

    var service = new UserService(userRepository, CLOCK);
    var result = service.create("a@b.com", "Alice");

    verify(userRepository).save(userCaptor.getValue());
    assertThat(result.email()).isEqualTo("a@b.com");
    assertThat(result.displayName()).isEqualTo("Alice");
    assertThat(result.createdAt()).isEqualTo(NOW);
    assertThat(result.updatedAt()).isEqualTo(NOW);
    assertThat(result.id().value()).isNotNull();
  }

  @Test
  void create_throwsWhenEmailAlreadyExists() {
    var existing = new User(new UserId(UUID.randomUUID()), "a@b.com", "Existing", NOW, NOW);
    when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(existing));

    var service = new UserService(userRepository, CLOCK);

    assertThatThrownBy(() -> service.create("a@b.com", "Alice"))
        .isInstanceOf(UserAlreadyExistsException.class)
        .hasMessageContaining("a@b.com");
  }

  @Test
  void getById_returnsUserWhenFound() {
    var id = new UserId(UUID.randomUUID());
    var user = new User(id, "a@b.com", "Alice", NOW, NOW);
    when(userRepository.findById(id)).thenReturn(Optional.of(user));

    var service = new UserService(userRepository, CLOCK);

    assertThat(service.getById(id)).isSameAs(user);
  }

  @Test
  void getById_throwsWhenMissing() {
    var id = new UserId(UUID.randomUUID());
    when(userRepository.findById(id)).thenReturn(Optional.empty());

    var service = new UserService(userRepository, CLOCK);

    assertThatThrownBy(() -> service.getById(id))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessageContaining(id.value().toString());
  }

  @Test
  void list_returnsAllUsers() {
    when(userRepository.findAll()).thenReturn(List.of(
        new User(new UserId(UUID.randomUUID()), "a@b.com", "Alice", NOW, NOW),
        new User(new UserId(UUID.randomUUID()), "c@d.com", "Chris", NOW, NOW)
    ));

    var service = new UserService(userRepository, CLOCK);

    assertThat(service.list()).hasSize(2);
  }

  @Test
  void update_changesEmailAndDisplayName_andBumpsUpdatedAt() {
    var id = new UserId(UUID.randomUUID());
    var existing = new User(id, "old@b.com", "Old", Instant.parse("2025-01-01T00:00:00Z"), Instant.parse("2025-01-02T00:00:00Z"));
    when(userRepository.findById(id)).thenReturn(Optional.of(existing));
    when(userRepository.findByEmail("new@b.com")).thenReturn(Optional.empty());
    when(userRepository.save(userCaptor.capture())).thenAnswer(inv -> inv.getArgument(0));

    var service = new UserService(userRepository, CLOCK);
    var updated = service.update(id, "new@b.com", "New Name");

    assertThat(updated.id()).isEqualTo(id);
    assertThat(updated.email()).isEqualTo("new@b.com");
    assertThat(updated.displayName()).isEqualTo("New Name");
    assertThat(updated.createdAt()).isEqualTo(existing.createdAt());
    assertThat(updated.updatedAt()).isEqualTo(NOW);
  }

  @Test
  void update_throwsWhenEmailBelongsToDifferentUser() {
    var id = new UserId(UUID.randomUUID());
    var existing = new User(id, "old@b.com", "Old", NOW, NOW);
    when(userRepository.findById(id)).thenReturn(Optional.of(existing));

    var other = new User(new UserId(UUID.randomUUID()), "taken@b.com", "Other", NOW, NOW);
    when(userRepository.findByEmail("taken@b.com")).thenReturn(Optional.of(other));

    var service = new UserService(userRepository, CLOCK);

    assertThatThrownBy(() -> service.update(id, "taken@b.com", "New"))
        .isInstanceOf(UserAlreadyExistsException.class)
        .hasMessageContaining("taken@b.com");
  }

  @Test
  void delete_succeedsWhenRepositoryDeletes() {
    var id = new UserId(UUID.randomUUID());
    when(userRepository.deleteById(id)).thenReturn(true);

    var service = new UserService(userRepository, CLOCK);
    service.delete(id);

    verify(userRepository).deleteById(id);
  }

  @Test
  void delete_throwsWhenRepositoryReturnsFalse() {
    var id = new UserId(UUID.randomUUID());
    when(userRepository.deleteById(id)).thenReturn(false);

    var service = new UserService(userRepository, CLOCK);

    assertThatThrownBy(() -> service.delete(id))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessageContaining(id.value().toString());
  }
}

