package com.dv.genaitraining.features.dancer.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Supported dance styles.
 */
public enum DanceStyle {
  BACHATA,
  SALSA,
  KIZOMBA,
  ZOUK;

  /**
   * Parses a style from a human-friendly string.
   *
   * @param value style (case-insensitive; e.g. \"Salsa\")
   * @return enum value
   */
  @JsonCreator
  public static DanceStyle fromString(String value) {
    if (value == null) {
      throw new IllegalArgumentException("style must not be null");
    }
    String normalized = value.trim().toUpperCase();
    return switch (normalized) {
      case "BACHATA" -> BACHATA;
      case "SALSA" -> SALSA;
      case "KIZOMBA" -> KIZOMBA;
      case "ZOUK" -> ZOUK;
      default -> throw new IllegalArgumentException("Unknown style: " + value);
    };
  }

  /**
   * Serializes to a simple lowercase string (e.g. \"salsa\").
   *
   * @return lowercase style name
   */
  @JsonValue
  public String toJson() {
    return name().toLowerCase();
  }
}

