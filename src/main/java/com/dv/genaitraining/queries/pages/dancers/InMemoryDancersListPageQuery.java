package com.dv.genaitraining.queries.pages.dancers;

import com.dv.genaitraining.features.dancer.domain.DanceStyle;
import com.dv.genaitraining.features.dancer.domain.Dancer;
import com.dv.genaitraining.features.dancer.domain.DancerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InMemoryDancersListPageQuery implements DancersListPageQuery {
  private final DancerRepository dancerRepository;

  public InMemoryDancersListPageQuery(DancerRepository dancerRepository) {
    this.dancerRepository = dancerRepository;
  }

  @Override
  public DancersListPageView load() {
    List<DancersListPageView.Item> items = dancerRepository.findAll().stream()
        .map(InMemoryDancersListPageQuery::toItem)
        .toList();
    return new DancersListPageView(items);
  }

  private static DancersListPageView.Item toItem(Dancer d) {
    return new DancersListPageView.Item(
        d.id().value(),
        d.name() == null ? "" : d.name(),
        d.roles().stream().map(r -> r.name().toLowerCase()).toList(),
        d.styles().stream().map(DanceStyle::toJson).toList(),
        d.createdAt()
    );
  }
}

