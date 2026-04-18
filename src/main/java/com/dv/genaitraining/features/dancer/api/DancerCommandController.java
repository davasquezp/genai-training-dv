package com.dv.genaitraining.features.dancer.api;

import com.dv.genaitraining.features.dancer.application.RegisterDancerUseCase;
import com.dv.genaitraining.features.dancer.domain.DanceStyle;
import com.dv.genaitraining.features.dancer.domain.Dancer;
import com.dv.genaitraining.features.dancer.domain.DancerRepository;
import com.dv.genaitraining.features.dancer.domain.Role;
import com.dv.genaitraining.shared.ids.MemberId;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * Write-only REST adapter for dancer registration and profile updates.
 */
@RestController
@RequestMapping("/api/dancers")
public class DancerCommandController {
  private final RegisterDancerUseCase register;
  private final DancerRepository dancerRepository;

  public DancerCommandController(RegisterDancerUseCase register, DancerRepository dancerRepository) {
    this.register = register;
    this.dancerRepository = dancerRepository;
  }

  @PostMapping
  public ResponseEntity<DancerResponse> register(@Valid @RequestBody RegisterDancerRequest request) {
    List<Role> roles = parseRoles(request.role());
    Dancer created = register.register(
        request.name(),
        roles.isEmpty() ? null : roles.getFirst(),
        request.styles()
    );
    return ResponseEntity
        .created(URI.create("/api/dancers/" + created.id().value()))
        .body(toResponse(created));
  }

  @PutMapping("/me")
  public ResponseEntity<DancerMeResponse> updateMe(
      Authentication authentication,
      @Valid @RequestBody UpdateMyDancerRequest request
  ) {
    MemberId memberId = memberId(authentication);
    if (memberId == null) {
      return ResponseEntity.status(401).build();
    }

    Dancer existing = dancerRepository.findByMemberId(memberId).orElse(null);
    if (existing == null) {
      return ResponseEntity.notFound().build();
    }

    List<Role> roles = request.dancerRoles() == null ? List.of() : List.copyOf(request.dancerRoles());
    String name = request.name() == null ? "" : request.name().trim();

    Dancer updated = new Dancer(
        existing.id(),
        existing.memberId(),
        name,
        roles,
        existing.styles(),
        existing.createdAt()
    );
    Dancer saved = dancerRepository.save(updated);
    return ResponseEntity.ok(toMeResponse(saved));
  }

  private static DancerResponse toResponse(Dancer d) {
    return new DancerResponse(
        d.id().value(),
        d.name() == null ? "" : d.name(),
        d.roles().stream().map(r -> r.name().toLowerCase()).toList(),
        d.styles().stream().map(DanceStyle::toJson).toList(),
        d.createdAt()
    );
  }

  private static DancerMeResponse toMeResponse(Dancer d) {
    return new DancerMeResponse(
        d.id().value(),
        d.name(),
        d.roles().stream().map(Role::name).toList()
    );
  }

  private static MemberId memberId(Authentication authentication) {
    if (authentication == null || authentication.getPrincipal() == null) return null;
    UUID id = UUID.fromString(String.valueOf(authentication.getPrincipal()));
    return new MemberId(id);
  }

  private static List<Role> parseRoles(String raw) {
    String normalized = raw == null ? "" : raw.trim().toLowerCase();
    return switch (normalized) {
      case "lead" -> List.of(Role.LEAD);
      case "follower" -> List.of(Role.FOLLOWER);
      case "" -> List.of();
      default -> throw new IllegalArgumentException("Invalid role: " + raw);
    };
  }
}
