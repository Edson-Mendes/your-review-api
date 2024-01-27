package br.com.emendes.yourreviewapi.util.component;

import br.com.emendes.yourreviewapi.exception.UserIsNotAuthenticatedException;
import br.com.emendes.yourreviewapi.model.entity.User;

/**
 * Interface com as abstrações responsáveis por manipular o usuário da requisição atual.
 */
public interface AuthenticatedUserComponent {

  /**
   * Busca o atual usuário autenticado.
   *
   * @return User usuário autenticado na requisição atual.
   * @throws UserIsNotAuthenticatedException caso não tenha usuário autenticado na requisição atual.
   */
  User getCurrentUser();

}
