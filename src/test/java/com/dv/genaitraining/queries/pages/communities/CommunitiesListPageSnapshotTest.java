package com.dv.genaitraining.queries.pages.communities;

import com.dv.genaitraining.features.community.domain.Community;
import com.dv.genaitraining.features.community.domain.CommunityLocation;
import com.dv.genaitraining.features.community.domain.CommunityRepository;
import com.dv.genaitraining.features.communitymembership.application.AssociateDancerToCommunityUseCase;
import com.dv.genaitraining.features.dancer.application.RegisterDancerUseCase;
import com.dv.genaitraining.queries.snapshots.PageQuerySnapshotSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommunitiesListPageSnapshotTest {
  @Autowired MockMvc mvc;
  @Autowired CommunityRepository communityRepository;
  @Autowired RegisterDancerUseCase registerDancer;
  @Autowired AssociateDancerToCommunityUseCase associate;

  @Test
  void snapshot_anonymous() throws Exception {
    UUID communityId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    seedCommunity(communityId, Instant.parse("2026-01-01T00:00:00Z"));

    var dancer = registerDancer.register("Alice", null, java.util.List.of());
    associate.associate(dancer.id().value(), communityId);

    String json = mvc.perform(get("/api/page/communities").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    PageQuerySnapshotSupport.assertMatchesSnapshot(json, snapshotPath("communities_anonymous.json"));
  }

  private void seedCommunity(UUID id, Instant createdAt) {
    communityRepository.save(new Community(
        new com.dv.genaitraining.shared.ids.CommunityId(id),
        "Test community",
        "A description",
        null,
        true,
        (CommunityLocation) null,
        createdAt
    ));
  }

  private static Path snapshotPath(String filename) {
    return Path.of("src/test/resources/queries").resolve(filename);
  }
}

