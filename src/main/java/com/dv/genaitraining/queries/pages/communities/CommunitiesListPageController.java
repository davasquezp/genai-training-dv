package com.dv.genaitraining.queries.pages.communities;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/page/communities")
public class CommunitiesListPageController {
  private final CommunitiesListPageQuery query;

  public CommunitiesListPageController(CommunitiesListPageQuery query) {
    this.query = query;
  }

  @GetMapping
  public CommunitiesListPageView load() {
    return query.load();
  }
}

