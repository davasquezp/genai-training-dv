package com.dv.genaitraining.features.communitymembership.api;

import com.dv.genaitraining.features.communitymembership.application.AssociateDancerToCommunityUseCase;
import com.dv.genaitraining.features.communitymembership.application.ListCommunityMembershipsUseCase;
import com.dv.genaitraining.features.communitymembership.domain.CommunityMembership;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * REST adapter for dancer-community memberships.
 */
@RestController
@RequestMapping("/api/community-memberships")
public class CommunityMembershipController {
  private final AssociateDancerToCommunityUseCase associate;
  private final ListCommunityMembershipsUseCase list;

  /**
   * Creates the controller.
   *
   * @param associate association use-case
   * @param list list use-case
   */
  public CommunityMembershipController(
      AssociateDancerToCommunityUseCase associate,
      ListCommunityMembershipsUseCase list
  ) {
    this.associate = associate;
    this.list = list;
  }

  /**
   * Associates a dancer to a community.
   *
   * @param request request payload
   * @return membership
   */
  @PostMapping
  public ResponseEntity<CommunityMembershipResponse> associate(
      @Valid @RequestBody AssociateCommunityMembershipRequest request
  ) {
    CommunityMembership created = associate.associate(request.dancerId(), request.communityId());
    return ResponseEntity
        .created(URI.create("/api/community-memberships/" + created.id().value()))
        .body(toResponse(created));
  }

  /**
   * Lists memberships by community or dancer.
   *
   * @param communityId optional community id filter
   * @param dancerId optional dancer id filter
   * @return memberships
   */
  @GetMapping
  public List<CommunityMembershipResponse> list(
      @RequestParam(required = false) UUID communityId,
      @RequestParam(required = false) UUID dancerId
  ) {
    if (communityId != null) {
      return list.listByCommunity(communityId).stream().map(CommunityMembershipController::toResponse).toList();
    }
    if (dancerId != null) {
      return list.listByDancer(dancerId).stream().map(CommunityMembershipController::toResponse).toList();
    }
    throw new IllegalArgumentException("Provide either communityId or dancerId query parameter");
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
