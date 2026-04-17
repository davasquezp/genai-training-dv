package com.dv.genaitraining.features.community.application;

import com.dv.genaitraining.features.community.domain.Community;
import java.util.List;

/**
 * Read use-case for listing communities.
 */
public interface ListCommunitiesUseCase {
  /**
   * Lists communities.
   *
   * @return communities
   */
  List<Community> list();
}
