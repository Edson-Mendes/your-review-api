package br.com.emendes.yourreviewapi.service;

import br.com.emendes.yourreviewapi.dto.request.UserRegisterRequest;
import br.com.emendes.yourreviewapi.dto.response.UserDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.UserSummaryResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  /**
   * Busca paginada de usuários.
   *
   * @param pageable modo como a busca será paginada.
   * @return {@code Page<UserSummaryResponse>} paginação com resumo de usuários.
   */
  Page<UserSummaryResponse> fetch(Pageable pageable);

}
