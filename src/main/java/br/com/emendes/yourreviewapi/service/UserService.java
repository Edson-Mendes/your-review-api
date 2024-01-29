package br.com.emendes.yourreviewapi.service;

import br.com.emendes.yourreviewapi.dto.request.UserRegisterRequest;
import br.com.emendes.yourreviewapi.dto.response.UserDetailsResponse;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

/**
 * Interface com as abstrações para manipulação do recurso User.
 */
@Validated
public interface UserService {

  /**
   * Cadastrar um usuário no sistema.
   *
   * @param userRegisterRequest objeto contendo os dados para criação do usuário.
   * @return UserDetailsResponse contendo dados detalhados do usuário cadastrado.
   */
  UserDetailsResponse register(@Valid UserRegisterRequest userRegisterRequest);

}
