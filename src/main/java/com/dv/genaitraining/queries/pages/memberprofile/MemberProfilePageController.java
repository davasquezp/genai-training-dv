package com.dv.genaitraining.queries.pages.memberprofile;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/page/me")
public class MemberProfilePageController {
  private final MemberProfilePageQuery query;

  public MemberProfilePageController(MemberProfilePageQuery query) {
    this.query = query;
  }

  @GetMapping
  public ResponseEntity<MemberProfilePageView> load(Authentication authentication) {
    try {
      return ResponseEntity.ok(query.load(authentication));
    } catch (IllegalArgumentException e) {
      String msg = e.getMessage() == null ? "" : e.getMessage();
      if (msg.toLowerCase().contains("unauthorized")) return ResponseEntity.status(401).build();
      throw e;
    }
  }
}

