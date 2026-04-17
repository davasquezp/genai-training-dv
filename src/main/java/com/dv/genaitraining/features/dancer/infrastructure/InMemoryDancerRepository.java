package com.dv.genaitraining.features.dancer.infrastructure;

import com.dv.genaitraining.features.dancer.domain.Dancer;
import com.dv.genaitraining.features.dancer.domain.DancerRepository;
import com.dv.genaitraining.shared.ids.DancerId;
import com.dv.genaitraining.shared.ids.MemberId;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory adapter for dancer persistence.
 */
@Repository
public class InMemoryDancerRepository implements DancerRepository {
  private final Map<java.util.UUID, Dancer> byId = new ConcurrentHashMap<>();

  @Override
  public Dancer save(Dancer dancer) {
    byId.put(dancer.id().value(), dancer);
    return dancer;
  }

  @Override
  public List<Dancer> findAll() {
    return byId.values().stream()
        .sorted(Comparator.comparing(Dancer::createdAt).reversed())
        .toList();
  }

  @Override
  public Optional<Dancer> findById(DancerId id) {
    return Optional.ofNullable(byId.get(id.value()));
  }

  @Override
  public Optional<Dancer> findByMemberId(MemberId memberId) {
    if (memberId == null) return Optional.empty();
    return byId.values().stream()
        .filter(d -> d.memberId() != null && memberId.equals(d.memberId()))
        .findFirst();
  }
}

