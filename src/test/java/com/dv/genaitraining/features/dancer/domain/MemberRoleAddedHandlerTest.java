package com.dv.genaitraining.features.dancer.domain;

import com.dv.genaitraining.features.member.domain.MemberRole;
import com.dv.genaitraining.shared.events.EventBus;
import com.dv.genaitraining.shared.events.member.MemberRoleAdded;
import com.dv.genaitraining.shared.ids.DancerId;
import com.dv.genaitraining.shared.ids.MemberId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberRoleAddedHandlerTest {
  private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");
  private static final Clock CLOCK = Clock.fixed(NOW, ZoneOffset.UTC);

  @Mock DancerRepository dancerRepository;
  @Mock EventBus eventBus;
  @Captor ArgumentCaptor<Dancer> dancerCaptor;

  @Test
  void handle_isIdempotent_whenDancerAlreadyExists() {
    MemberId memberId = MemberId.newId();
    when(dancerRepository.findByMemberId(memberId)).thenReturn(Optional.of(dummyDancer(memberId)));

    var handler = new MemberRoleAddedHandler(dancerRepository, eventBus, CLOCK);
    handler.handle(new MemberRoleAdded(memberId, MemberRole.DANCER));

    verify(dancerRepository, never()).save(org.mockito.ArgumentMatchers.any());
  }

  @Test
  void handle_createsDancer_whenRoleIsDancer_andNoExistingDancer() {
    MemberId memberId = MemberId.newId();
    when(dancerRepository.findByMemberId(memberId)).thenReturn(Optional.empty());
    when(dancerRepository.save(dancerCaptor.capture())).thenAnswer(inv -> inv.getArgument(0));

    var handler = new MemberRoleAddedHandler(dancerRepository, eventBus, CLOCK);
    handler.handle(new MemberRoleAdded(memberId, MemberRole.DANCER));

    verify(dancerRepository).save(dancerCaptor.getValue());
    assertThat(dancerCaptor.getValue().memberId()).isEqualTo(memberId);
  }

  private static Dancer dummyDancer(MemberId memberId) {
    return new Dancer(
        DancerId.newId(),
        memberId,
        "",
        java.util.List.of(),
        java.util.List.of(),
        NOW
    );
  }
}

