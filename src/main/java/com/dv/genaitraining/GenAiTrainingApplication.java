package com.dv.genaitraining;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot entrypoint for the REST API application.
 *
 * <p>This class intentionally lives in the root package so Spring Boot can
 * auto-detect components, entities, and Spring Data repositories in all sub-packages.</p>
 */
@SpringBootApplication
public class GenAiTrainingApplication {
  /**
   * Starts the Spring Boot application.
   *
   * @param args CLI args
   */
  public static void main(String[] args) {
    SpringApplication.run(GenAiTrainingApplication.class, args);
  }
}

