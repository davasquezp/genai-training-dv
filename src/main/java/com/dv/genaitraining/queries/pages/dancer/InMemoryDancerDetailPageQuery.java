package com.dv.genaitraining.queries.pages.dancer;

import com.dv.genaitraining.features.dancer.domain.DanceStyle;
import com.dv.genaitraining.features.dancer.domain.Dancer;
import com.dv.genaitraining.features.dancer.domain.DancerRepository;
import com.dv.genaitraining.shared.ids.DancerId;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InMemoryDancerDetailPageQuery implements DancerDetailPageQuery {
  private final DancerRepository dancerRepository;

  public InMemoryDancerDetailPageQuery(DancerRepository dancerRepository) {
    this.dancerRepository = dancerRepository;
  }

  @Override
  public DancerDetailPageView load(UUID dancerIdValue) {
    if (dancerIdValue == null) throw new IllegalArgumentException("dancerId is required");
    Dancer dancer = dancerRepository.findById(new DancerId(dancerIdValue)).orElse(null);
    if (dancer == null) {
      throw new IllegalArgumentException("Dancer not found: " + dancerIdValue);
    }
    return new DancerDetailPageView(toDancer(dancer));
  }

  private static DancerDetailPageView.Dancer toDancer(Dancer d) {
    return new DancerDetailPageView.Dancer(
        d.id().value(),
        d.name() == null ? "" : d.name(),
        d.roles().stream().map(r -> r.name().toLowerCase()).toList(),
        d.styles().stream().map(DanceStyle::toJson).toList(),
        d.createdAt()
    );
  }
}

