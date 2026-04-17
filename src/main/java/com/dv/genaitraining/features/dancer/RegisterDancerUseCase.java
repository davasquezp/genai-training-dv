package com.dv.genaitraining.features.dancer;

/**
 * Inbound port for registering dancers (interest).
 */
public interface RegisterDancerUseCase {
  /**
   * Registers a dancer interest.
   *
   * @param name name
   * @param role lead/follower
   * @param styles styles
   * @return created dancer registration
   */
  Dancer register(String name, Role role, java.util.List<DanceStyle> styles);
}

