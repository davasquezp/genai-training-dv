package com.dv.genaitraining.queries.pages.memberprofile;

import org.springframework.security.core.Authentication;

public interface MemberProfilePageQuery {
  MemberProfilePageView load(Authentication authentication);
}

