package com.dv.genaitraining.features.community.application;

import com.dv.genaitraining.features.community.domain.Community;
import com.dv.genaitraining.features.community.domain.CommunityLocation;

/**
 * Use-case for creating communities.
 */
public interface CreateCommunityUseCase {
  /**
   * Creates a community.
   *
   * @param name name
   * @param description description
   * @param imageDataUrl optional image data-url
   * @param global global flag
   * @param location optional location
   * @return created community
   */
  Community create(
      String name,
      String description,
      String imageDataUrl,
      boolean global,
      CommunityLocation location
  );
}
