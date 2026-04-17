package com.dv.genaitraining.features.member;

import com.dv.genaitraining.shared.ids.MemberId;
import com.dv.genaitraining.shared.events.EventBus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
  private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");
  private static final Clock CLOCK = Clock.fixed(NOW, ZoneOffset.UTC);

  @Mock MemberRepository repository;
  @Mock EventBus eventBus;

  @Test
  void signup_assignsNoneRole() {
    when(repository.findByEmail("a@example.com")).thenReturn(Optional.empty());
    when(repository.findByPhone("+1")).thenReturn(Optional.empty());
    when(repository.save(org.mockito.ArgumentMatchers.any())).thenAnswer(inv -> inv.getArgument(0));

    var service = new MemberService(repository, eventBus, CLOCK);
    Member created = service.signup("A@Example.com", "Daniel", "+1", "secret");

    assertThat(created.email()).isEqualTo("a@example.com");
    assertThat(created.roles()).containsExactly(MemberRole.NONE);
    assertThat(created.createdAt()).isEqualTo(NOW);
    assertThat(created.passwordHash()).isNotBlank();
    assertThat(created.passwordSalt()).isNotBlank();
  }

  @Test
  void signup_failsWhenEmailExists() {
    when(repository.findByEmail("a@example.com")).thenReturn(Optional.of(dummyMember("a@example.com", "+2")));
    var service = new MemberService(repository, eventBus, CLOCK);
    assertThatThrownBy(() -> service.signup("a@example.com", "Daniel", "+1", "secret"))
        .isInstanceOf(MemberEmailAlreadyExistsException.class);
  }

  @Test
  void signup_failsWhenPhoneExists() {
    when(repository.findByEmail("a@example.com")).thenReturn(Optional.empty());
    when(repository.findByPhone("+1")).thenReturn(Optional.of(dummyMember("b@example.com", "+1")));
    var service = new MemberService(repository, eventBus, CLOCK);
    assertThatThrownBy(() -> service.signup("a@example.com", "Daniel", "+1", "secret"))
        .isInstanceOf(MemberPhoneAlreadyExistsException.class);
  }

  @Test
  void login_failsWhenPasswordIncorrect() {
    when(repository.findByEmail("a@example.com")).thenReturn(Optional.empty());
    when(repository.findByPhone("+1")).thenReturn(Optional.empty());
    when(repository.save(org.mockito.ArgumentMatchers.any())).thenAnswer(inv -> inv.getArgument(0));

    var service = new MemberService(repository, eventBus, CLOCK);
    Member created = service.signup("a@example.com", "Daniel", "+1", "secret");
    when(repository.findByEmail("a@example.com")).thenReturn(Optional.of(created));

    assertThatThrownBy(() -> service.login("a@example.com", "wrong"))
        .isInstanceOf(InvalidCredentialsException.class);
  }

  private static Member dummyMember(String email, String phone) {
    return new Member(
        MemberId.newId(),
        email,
        "Daniel",
        phone,
        "hash",
        "salt",
        null,
        null,
        null,
        null,
        java.util.Set.of(MemberRole.NONE),
        NOW
    );
  }
}

