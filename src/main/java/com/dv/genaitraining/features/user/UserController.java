package com.dv.genaitraining.features.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * REST adapter exposing User CRUD operations.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
  private final CreateUserUseCase create;
  private final GetUserUseCase get;
  private final UpdateUserUseCase update;
  private final DeleteUserUseCase delete;

  /**
   * Creates a controller.
   *
   * @param create create use-case
   * @param get get use-case
   * @param update update use-case
   * @param delete delete use-case
   */
  public UserController(
      CreateUserUseCase create,
      GetUserUseCase get,
      UpdateUserUseCase update,
      DeleteUserUseCase delete
  ) {
    this.create = create;
    this.get = get;
    this.update = update;
    this.delete = delete;
  }

  /**
   * Creates a new user.
   *
   * @param request create payload
   * @return created user
   */
  @PostMapping
  public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
    User created = create.create(request.email(), request.displayName());
    return ResponseEntity
        .created(URI.create("/api/users/" + created.id().value()))
        .body(toResponse(created));
  }

  /**
   * Gets a user by id.
   *
   * @param id user id
   * @return user
   */
  @GetMapping("/{id}")
  public UserResponse getById(@PathVariable UUID id) {
    return toResponse(get.getById(new UserId(id)));
  }

  /**
   * Lists users.
   *
   * @return users
   */
  @GetMapping
  public List<UserResponse> list() {
    return get.list().stream().map(UserController::toResponse).toList();
  }

  /**
   * Updates a user.
   *
   * @param id user id
   * @param request update payload
   * @return updated user
   */
  @PutMapping("/{id}")
  public UserResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest request) {
    return toResponse(update.update(new UserId(id), request.email(), request.displayName()));
  }

  /**
   * Deletes a user.
   *
   * @param id user id
   * @return 204
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    delete.delete(new UserId(id));
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  private static UserResponse toResponse(User u) {
    return new UserResponse(u.id().value(), u.email(), u.displayName(), u.createdAt(), u.updatedAt());
  }
}

