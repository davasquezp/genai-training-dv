package com.dv.genaitraining.queries.pages.dancer;

import java.util.UUID;

public interface DancerDetailPageQuery {
  DancerDetailPageView load(UUID dancerId);
}

