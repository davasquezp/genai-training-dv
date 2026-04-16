package com.dv.genaitraining.features.dancer;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * REST adapter for dancer registration.
 */
@RestController
@RequestMapping("/api/dancers")
public class DancerController {
  private final RegisterDancerUseCase register;
  private final ListDancersUseCase list;

  /**
   * Creates the controller.
   *
   * @param register register use-case
   * @param list list use-case
   */
  public DancerController(RegisterDancerUseCase register, ListDancersUseCase list) {
    this.register = register;
    this.list = list;
  }

  /**
   * Registers dancer interest.
   *
   * @param request request
   * @return created dancer
   */
  @PostMapping
  public ResponseEntity<DancerResponse> register(@Valid @RequestBody RegisterDancerRequest request) {
    Role role = parseRole(request.role());
    Dancer created = register.register(
        request.name(),
        role,
        request.country().code(),
        request.country().name(),
        request.styles()
    );
    return ResponseEntity
        .created(URI.create("/api/dancers/" + created.id().value()))
        .body(toResponse(created));
  }

  /**
   * Lists dancers (debug/admin).
   *
   * @return dancers
   */
  @GetMapping
  public List<DancerResponse> list() {
    return list.list().stream().map(DancerController::toResponse).toList();
  }

  private static DancerResponse toResponse(Dancer d) {
    return new DancerResponse(
        d.id().value(),
        d.name(),
        d.role().name().toLowerCase(),
        d.countryCode(),
        d.countryName(),
        d.styles().stream().map(DanceStyle::toJson).toList(),
        d.createdAt()
    );
  }

  private static Role parseRole(String raw) {
    String normalized = raw == null ? "" : raw.trim().toLowerCase();
    return switch (normalized) {
      case "lead" -> Role.LEAD;
      case "follower" -> Role.FOLLOWER;
      default -> throw new IllegalArgumentException("Invalid role: " + raw);
    };
  }
}

