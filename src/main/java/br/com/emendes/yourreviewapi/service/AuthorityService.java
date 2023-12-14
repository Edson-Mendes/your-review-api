package br.com.emendes.yourreviewapi.service;

import br.com.emendes.yourreviewapi.exception.AuthorityNotFoundException;
import br.com.emendes.yourreviewapi.model.entity.Authority;

/**
 * Interface com as abstrações para manipulação do recurso Authority.
 */
public interface AuthorityService {

  /**
   * Busca Authority por nome.
   *
   * @param name nome da Authority a ser buscada.
   * @return Objeto Authority.
   * @throws IllegalArgumentException   caso name seja null.
   * @throws AuthorityNotFoundException caso não seja encontrado uma Authority com o dado name.
   */
  Authority findByName(String name);

}
