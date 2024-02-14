package br.com.emendes.yourreviewapi.controller;

import br.com.emendes.yourreviewapi.dto.request.AuthenticationRequest;
import br.com.emendes.yourreviewapi.dto.response.AuthenticationResponse;
import br.com.emendes.yourreviewapi.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Classe controller reponsável pelo endpoint /api/v1/auth/**.
 */
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
  @PostMapping("/signin")
  public ResponseEntity<AuthenticationResponse> signIn(@RequestBody AuthenticationRequest authenticationRequest) {
    return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
  }

  /**
   * Método responsável por GET /api/v1/auth/refresh.
   */
  @GetMapping("/refresh")
  public ResponseEntity<AuthenticationResponse> refreshToken() {
    return ResponseEntity.ok(authenticationService.refreshToken());
  }

}
