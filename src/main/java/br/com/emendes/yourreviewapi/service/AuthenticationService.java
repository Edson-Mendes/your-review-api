package br.com.emendes.yourreviewapi.service;

import br.com.emendes.yourreviewapi.dto.request.AuthenticationRequest;
import br.com.emendes.yourreviewapi.dto.response.AuthenticationResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;

/**
 * Interface com as abstrações para autenticação de usuário.
 */
@Validated
public interface AuthenticationService {

  /**
   * Realiza a autenticação do usuário por username (email) e password.
   *
   * @param authenticationRequest objeto contendo as credenciais do usuário (username e password).
   * @return Objeto AuthenticationResponse contendo JWT que comprova a autenticidade do usuário.
   * @throws AuthenticationException – if authentication fails
   */
  AuthenticationResponse authenticate(@Valid AuthenticationRequest authenticationRequest);

  /**
   * Gera um novo JWT para o usuário autenticado.
   *
   * @return Objeto {@link AuthenticationResponse} contendo JWT que comprova a autenticidade do usuário.
   */
  AuthenticationResponse refreshToken();

}
