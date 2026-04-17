package com.dv.genaitraining.features.dancer.application;

import com.dv.genaitraining.features.dancer.domain.DanceStyle;
import com.dv.genaitraining.features.dancer.domain.Dancer;
import com.dv.genaitraining.features.dancer.domain.DancerRepository;
import com.dv.genaitraining.features.dancer.domain.Role;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DancerServiceTest {
  private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");
  private static final Clock CLOCK = Clock.fixed(NOW, ZoneOffset.UTC);

  @Mock DancerRepository dancerRepository;
  @Captor ArgumentCaptor<Dancer> dancerCaptor;

  @Test
  void register_createsNewDancerWithIdAndTimestamp() {
    when(dancerRepository.save(dancerCaptor.capture())).thenAnswer(inv -> inv.getArgument(0));

    var service = new DancerService(dancerRepository, CLOCK);
    var result = service.register(
        "Alice",
        Role.LEAD,
        List.of(DanceStyle.SALSA, DanceStyle.BACHATA)
    );

    verify(dancerRepository).save(dancerCaptor.getValue());
    assertThat(result.id().value()).isNotNull();
    assertThat(result.name()).isEqualTo("Alice");
    assertThat(result.roles()).containsExactly(Role.LEAD);
    assertThat(result.styles()).containsExactly(DanceStyle.SALSA, DanceStyle.BACHATA);
    assertThat(result.createdAt()).isEqualTo(NOW);
  }

  @Test
  void list_delegatesToRepository() {
    when(dancerRepository.findAll()).thenReturn(List.of());
    var service = new DancerService(dancerRepository, CLOCK);
    assertThat(service.list()).isEmpty();
  }
}

