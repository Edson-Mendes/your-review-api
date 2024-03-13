package br.com.emendes.yourreviewapi.controller;

import br.com.emendes.yourreviewapi.dto.request.AuthenticationRequest;
import br.com.emendes.yourreviewapi.dto.response.AuthenticationResponse;
import br.com.emendes.yourreviewapi.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.com.emendes.yourreviewapi.config.beans.OpenAPIBeans.SECURITY_SCHEME_KEY;

/**
 * Classe controller reponsável pelo endpoint /api/v1/auth/**.
 */
@Tag(name = "Authentication", description = "Authentication management APIs")
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
@RestController
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  /**
   * Método responsável por POST /api/v1/auth/signin.
   *
   * @param authenticationRequest objeto contendo as credenciais do usuário.
   */
  @Operation(
      summary = "Perform authentication",
      description = """
          Endpoint to client perform authentication.
          \nThe response, if successful, has status 200 with a JSON containing JWT.
          """
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful user authentication",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))),
      @ApiResponse(responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
  })
  @PostMapping("/signin")
  public ResponseEntity<AuthenticationResponse> signIn(@RequestBody AuthenticationRequest authenticationRequest) {
    return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
  }

  /**
   * Método responsável por GET /api/v1/auth/refresh.
   */
  @Operation(
      summary = "Request an refresh token",
      description = """
          Endpoint to client refresh authorization token, the client must request a refresh token before it expires.
          \nThe response, if successful, has status 200 with is a JSON containing JWT.
          \nThe client must be authenticated.
          """,
      security = {@SecurityRequirement(name = SECURITY_SCHEME_KEY)}
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful refresh token",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized: jwt authentication failed",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
  })
  @GetMapping("/refresh")
  public ResponseEntity<AuthenticationResponse> refreshToken() {
    return ResponseEntity.ok(authenticationService.refreshToken());
  }

}
