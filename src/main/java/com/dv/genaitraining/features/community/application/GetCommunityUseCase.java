package com.dv.genaitraining.features.community.application;

import com.dv.genaitraining.features.community.domain.Community;
import java.util.Optional;
import java.util.UUID;

/**
 * Read use-case for one community.
 */
public interface GetCommunityUseCase {
  /**
   * Gets a community by id.
   *
   * @param id community id
   * @return community if found
   */
  Optional<Community> get(UUID id);
}
