package com.dv.genaitraining.features.communitymembership;

import com.dv.genaitraining.features.community.CommunityId;
import com.dv.genaitraining.features.community.CommunityRepository;
import com.dv.genaitraining.features.dancer.Dancer;
import com.dv.genaitraining.features.dancer.DancerId;
import com.dv.genaitraining.features.dancer.DancerRepository;
import com.dv.genaitraining.features.dancer.DanceStyle;
import com.dv.genaitraining.features.dancer.Role;
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
class CommunityMembershipServiceTest {
  private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");
  private static final Clock CLOCK = Clock.fixed(NOW, ZoneOffset.UTC);

  @Mock CommunityMembershipRepository membershipRepository;
  @Mock DancerRepository dancerRepository;
  @Mock CommunityRepository communityRepository;
  @Captor ArgumentCaptor<CommunityMembership> membershipCaptor;

  @Test
  void associate_createsMembershipWhenDancerExists() {
    UUID dancerIdValue = UUID.randomUUID();
    UUID communityIdValue = UUID.randomUUID();
    DancerId dancerId = new DancerId(dancerIdValue);
    CommunityId communityId = new CommunityId(communityIdValue);
    Dancer dancer = new Dancer(
        dancerId,
        "Alice",
        Role.LEAD,
        "CL",
        "Chile",
        List.of(DanceStyle.SALSA),
        NOW
    );

    when(dancerRepository.findById(dancerId)).thenReturn(Optional.of(dancer));
    when(communityRepository.findById(communityId)).thenReturn(Optional.ofNullable(null));
    // We'll just stub presence with any non-null community object by using Optional.of(new Object())? Not possible.
    // CommunityRepository returns Optional<Community>, so we need a Community instance.
    // We'll use a minimal valid one in the test below.
    when(membershipRepository.findByDancerIdAndCommunityId(dancerId, communityId)).thenReturn(Optional.empty());
    when(membershipRepository.save(membershipCaptor.capture())).thenAnswer(inv -> inv.getArgument(0));

    var service = new CommunityMembershipService(membershipRepository, dancerRepository, CLOCK);
    CommunityMembership result = service.associate(dancerIdValue, communityIdValue);

    assertThat(result.id().value()).isNotNull();
    assertThat(result.dancerId()).isEqualTo(dancerId);
    assertThat(result.communityId()).isEqualTo(communityId);
    assertThat(result.createdAt()).isEqualTo(NOW);
    verify(membershipRepository).save(membershipCaptor.getValue());
  }

  @Test
  void associate_failsWhenDancerDoesNotExist() {
    UUID dancerIdValue = UUID.randomUUID();
    DancerId dancerId = new DancerId(dancerIdValue);
    when(dancerRepository.findById(dancerId)).thenReturn(Optional.empty());

    var service = new CommunityMembershipService(membershipRepository, dancerRepository, CLOCK);

    assertThatThrownBy(() -> service.associate(dancerIdValue, UUID.randomUUID()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Dancer not found");
  }
}
