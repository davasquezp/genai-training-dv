package com.dv.genaitraining.features.dancer;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Application service for dancer registration.
 */
@Service
public class DancerService implements RegisterDancerUseCase, ListDancersUseCase {
  private final DancerRepository dancerRepository;
  private final Clock clock;

  /**
   * Creates the service.
   *
   * @param dancerRepository repository
   * @param clock clock
   */
  public DancerService(DancerRepository dancerRepository, Clock clock) {
    this.dancerRepository = Objects.requireNonNull(dancerRepository, "dancerRepository");
    this.clock = Objects.requireNonNull(clock, "clock");
  }

  @Override
  public Dancer register(String name, Role role, String countryCode, String countryName, List<DanceStyle> styles) {
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(role, "role");
    Objects.requireNonNull(countryCode, "countryCode");
    Objects.requireNonNull(countryName, "countryName");
    Objects.requireNonNull(styles, "styles");

    Instant now = Instant.now(clock);
    Dancer dancer = new Dancer(
        DancerId.newId(),
        name,
        role,
        countryCode,
        countryName,
        List.copyOf(styles),
        now
    );
    return dancerRepository.save(dancer);
  }

  @Override
  public List<Dancer> list() {
    return dancerRepository.findAll();
  }
}

