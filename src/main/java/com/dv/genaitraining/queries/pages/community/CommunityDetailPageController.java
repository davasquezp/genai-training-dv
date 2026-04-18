package com.dv.genaitraining.queries.pages.community;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/page/communities")
public class CommunityDetailPageController {
  private final CommunityDetailPageQuery query;

  public CommunityDetailPageController(CommunityDetailPageQuery query) {
    this.query = query;
  }

  @GetMapping("/{id}")
  public ResponseEntity<CommunityDetailPageView> load(@PathVariable UUID id, Authentication authentication) {
    try {
      return ResponseEntity.ok(query.load(id, authentication));
    } catch (IllegalArgumentException e) {
      String msg = e.getMessage() == null ? "" : e.getMessage();
      if (msg.toLowerCase().contains("not found")) return ResponseEntity.notFound().build();
      throw e;
    }
  }
}

