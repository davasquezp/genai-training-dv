package com.dv.genaitraining.features.community;

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
