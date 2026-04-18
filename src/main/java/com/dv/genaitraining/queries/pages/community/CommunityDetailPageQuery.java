package com.dv.genaitraining.queries.pages.community;

import java.util.UUID;

public interface CommunityDetailPageQuery {
  CommunityDetailPageView load(UUID communityId, org.springframework.security.core.Authentication authentication);
}

