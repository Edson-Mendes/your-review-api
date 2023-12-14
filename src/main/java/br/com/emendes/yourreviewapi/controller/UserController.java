package br.com.emendes.yourreviewapi.controller;

import br.com.emendes.yourreviewapi.dto.request.UserRegisterRequest;
import br.com.emendes.yourreviewapi.dto.response.UserDetailsResponse;
import br.com.emendes.yourreviewapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Classe controller reponsável pelo endpoint /api/v1/users/**.
 */
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users")
@RestController
public class UserController {

  private final UserService userService;

  /**
   * Método responsável por POST /api/v1/users.
   *
   * @param userRegisterRequest objeto com os dados de criação de usuário.
   */
  @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<UserDetailsResponse> register(
      @RequestBody UserRegisterRequest userRegisterRequest, UriComponentsBuilder uriBuilder) {
    UserDetailsResponse userDetailsResponse = userService.register(userRegisterRequest);
    URI location = uriBuilder.path("/api/v1/users/{id}").build(userDetailsResponse.id());

    return ResponseEntity.created(location).body(userDetailsResponse);
  }

}
