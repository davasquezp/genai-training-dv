package com.dv.genaitraining.features.community.infrastructure;

import com.dv.genaitraining.features.community.domain.Community;
import com.dv.genaitraining.features.community.domain.CommunityRepository;
import com.dv.genaitraining.shared.ids.CommunityId;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory adapter for community persistence.
 */
@Repository
public class InMemoryCommunityRepository implements CommunityRepository {
  private final Map<java.util.UUID, Community> byId = new ConcurrentHashMap<>();

  @Override
  public Community save(Community community) {
    byId.put(community.id().value(), community);
    return community;
  }

  @Override
  public List<Community> findAll() {
    return byId.values().stream()
        .sorted(Comparator.comparing(Community::createdAt).reversed())
        .toList();
  }

  @Override
  public Optional<Community> findById(CommunityId id) {
    return Optional.ofNullable(byId.get(id.value()));
  }
}
