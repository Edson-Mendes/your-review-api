package br.com.emendes.yourreviewapi.util.faker;

import br.com.emendes.yourreviewapi.model.entity.Authority;

/**
 * Classe para manter objetos fake relacionados a Authority para uso em testes automatizados.
 */
public class AuthorityFaker {

  private AuthorityFaker() {
  }

  /**
   * Retorna uma Authority de name USER.
   */
  public static Authority userAuthority() {
    return new Authority(1, "USER");
  }

  /**
   * Retorna uma Authority de name ADMIN.
   */
  public static Authority adminAuthority() {
    return new Authority(2, "ADMIN");
  }
}
