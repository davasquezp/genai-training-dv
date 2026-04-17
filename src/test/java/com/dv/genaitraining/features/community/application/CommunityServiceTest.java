package com.dv.genaitraining.features.community.application;

import com.dv.genaitraining.features.community.domain.Community;
import com.dv.genaitraining.features.community.domain.CommunityCountry;
import com.dv.genaitraining.features.community.domain.CommunityLocation;
import com.dv.genaitraining.features.community.domain.CommunityRepository;
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
class CommunityServiceTest {
  private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");
  private static final Clock CLOCK = Clock.fixed(NOW, ZoneOffset.UTC);

  @Mock CommunityRepository repository;
  @Captor ArgumentCaptor<Community> communityCaptor;

  @Test
  void create_createsCommunityWithUuidAndTimestamp() {
    when(repository.save(communityCaptor.capture())).thenAnswer(inv -> inv.getArgument(0));
    var service = new CommunityService(repository, CLOCK);

    Community created = service.create(
        "Santiago Bachata",
        "Community for social dancers",
        null,
        false,
        new CommunityLocation(new CommunityCountry("CL", "Chile"), "Metropolitana", "Santiago")
    );

    verify(repository).save(communityCaptor.getValue());
    assertThat(created.id().value()).isNotNull();
    assertThat(created.name()).isEqualTo("Santiago Bachata");
    assertThat(created.description()).isEqualTo("Community for social dancers");
    assertThat(created.global()).isFalse();
    assertThat(created.location()).isNotNull();
    assertThat(created.createdAt()).isEqualTo(NOW);
  }

  @Test
  void list_delegatesToRepository() {
    when(repository.findAll()).thenReturn(List.of());
    var service = new CommunityService(repository, CLOCK);
    assertThat(service.list()).isEmpty();
  }
}
