package com.dv.genaitraining.features.community.domain;

import com.dv.genaitraining.shared.ids.CommunityId;
import java.util.List;
import java.util.Optional;

/**
 * Outbound port for community persistence.
 */
public interface CommunityRepository {
  /**
   * Saves a community.
   *
   * @param community community
   * @return saved community
   */
  Community save(Community community);

  /**
   * Lists all communities.
   *
   * @return communities
   */
  List<Community> findAll();

  /**
   * Finds a community by id.
   *
   * @param id community id
   * @return community if found
   */
  Optional<Community> findById(CommunityId id);
}
