package com.dv.genaitraining.features.dancer;

import java.util.List;

/**
 * Inbound port for listing dancers.
 */
public interface ListDancersUseCase {
  /**
   * Lists dancers.
   *
   * @return dancers
   */
  List<Dancer> list();
}

