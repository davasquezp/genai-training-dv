package com.dv.genaitraining.features.dancer;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port for dancer persistence.
 */
public interface DancerRepository {
  /**
   * Saves a dancer registration.
   *
   * @param dancer dancer
   * @return saved dancer
   */
  Dancer save(Dancer dancer);

  /**
   * Lists all dancers (newest first).
   *
   * @return dancers
   */
  List<Dancer> findAll();

  /**
   * Finds a dancer by id.
   *
   * @param id dancer id
   * @return dancer if found
   */
  Optional<Dancer> findById(DancerId id);
}

