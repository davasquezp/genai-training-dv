package com.dv.genaitraining.features.communitymembership.api;

import com.dv.genaitraining.features.communitymembership.application.AssociateDancerToCommunityUseCase;
import com.dv.genaitraining.features.communitymembership.domain.CommunityMembership;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * Write-only REST adapter for dancer-community memberships.
 */
@RestController
@RequestMapping("/api/community-memberships")
public class CommunityMembershipCommandController {
  private final AssociateDancerToCommunityUseCase associate;

  public CommunityMembershipCommandController(AssociateDancerToCommunityUseCase associate) {
    this.associate = associate;
  }

  @PostMapping
  public ResponseEntity<CommunityMembershipResponse> associate(
      @Valid @RequestBody AssociateCommunityMembershipRequest request
  ) {
    CommunityMembership created = associate.associate(request.dancerId(), request.communityId());
    return ResponseEntity
        .created(URI.create("/api/community-memberships/" + created.id().value()))
        .body(toResponse(created));
  }

  private static CommunityMembershipResponse toResponse(CommunityMembership membership) {
    return new CommunityMembershipResponse(
        membership.id().value(),
        membership.dancerId().value(),
        membership.communityId().value(),
        membership.createdAt()
    );
  }
}
