package com.dv.genaitraining.features.dancer;

import java.util.List;

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
}

