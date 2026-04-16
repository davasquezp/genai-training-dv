package com.dv.genaitraining.shared;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Cross-cutting CORS configuration for browser-based clients.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
  private final String allowedOrigin;

  /**
   * Creates a CORS config.
   *
   * @param allowedOrigin allowed origin (e.g. https://your-pages-domain.pages.dev). Empty disables CORS config.
   */
  public CorsConfig(@Value("${APP_CORS_ALLOWED_ORIGIN:}") String allowedOrigin) {
    this.allowedOrigin = allowedOrigin == null ? "" : allowedOrigin.trim();
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    if (allowedOrigin.isEmpty()) {
      return;
    }
    registry.addMapping("/api/**")
        .allowedOrigins(allowedOrigin)
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("Content-Type", "Authorization")
        .allowCredentials(false);
  }
}

