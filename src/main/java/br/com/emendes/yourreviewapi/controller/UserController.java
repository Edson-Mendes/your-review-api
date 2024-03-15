package br.com.emendes.yourreviewapi.controller;

import br.com.emendes.yourreviewapi.dto.request.UserRegisterRequest;
import br.com.emendes.yourreviewapi.dto.response.UserDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.UserSummaryResponse;
import br.com.emendes.yourreviewapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static br.com.emendes.yourreviewapi.config.beans.OpenAPIBeans.SECURITY_SCHEME_KEY;

/**
 * Classe controller reponsável pelo endpoint /api/v1/users/**.
 */
@Tag(name = "User", description = "User management APIs")
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
  @Operation(
      summary = "Register user",
      description = """
          Endpoint to register user.
          \nThe response, if successful, has status 201 with a JSON containing information about user registered.
          """
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successful register review",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDetailsResponse.class))),
      @ApiResponse(responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
  })
  @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<UserDetailsResponse> register(
      @RequestBody UserRegisterRequest userRegisterRequest, UriComponentsBuilder uriBuilder) {
    UserDetailsResponse userDetailsResponse = userService.register(userRegisterRequest);
    URI location = uriBuilder.path("/api/v1/users/{id}").build(userDetailsResponse.id());

    return ResponseEntity.created(location).body(userDetailsResponse);
  }

  /**
   * Método responsável por GET /api/v1/users.
   *
   * @param pageable objeto contendo a maneira como deve ser a paginação dos usuários.
   */
  @Operation(
      summary = "Fetch users",
      description = """
          Endpoint to fetch users.
          \nThe response, if successful, has status 200 with a JSON containing a pageable list of users.
          \nThe client must be authenticated and must be Admin.
          """,
      security = {@SecurityRequirement(name = SECURITY_SCHEME_KEY)}
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful fetch users",
          content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized: jwt authentication failed", content = @Content),
      @ApiResponse(responseCode = "403", description = "Forbidden: user does not have authorization", content = @Content)
  })
  @GetMapping
  public ResponseEntity<Page<UserSummaryResponse>> fetch(@PageableDefault(size = 20) @ParameterObject Pageable pageable) {
    return ResponseEntity.ok(userService.fetch(pageable));
  }

}
