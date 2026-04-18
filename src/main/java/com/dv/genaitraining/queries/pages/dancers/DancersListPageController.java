package com.dv.genaitraining.queries.pages.dancers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/page/dancers")
public class DancersListPageController {
  private final DancersListPageQuery query;

  public DancersListPageController(DancersListPageQuery query) {
    this.query = query;
  }

  @GetMapping
  public DancersListPageView load() {
    return query.load();
  }
}

