package com.dv.genaitraining.queries.pages.dancer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/page/dancers")
public class DancerDetailPageController {
  private final DancerDetailPageQuery query;

  public DancerDetailPageController(DancerDetailPageQuery query) {
    this.query = query;
  }

  @GetMapping("/{id}")
  public ResponseEntity<DancerDetailPageView> load(@PathVariable UUID id) {
    try {
      return ResponseEntity.ok(query.load(id));
    } catch (IllegalArgumentException e) {
      String msg = e.getMessage() == null ? "" : e.getMessage();
      if (msg.toLowerCase().contains("not found")) return ResponseEntity.notFound().build();
      throw e;
    }
  }
}

