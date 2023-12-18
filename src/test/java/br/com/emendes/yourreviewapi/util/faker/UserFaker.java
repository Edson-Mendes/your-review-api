package br.com.emendes.yourreviewapi.util.faker;

import br.com.emendes.yourreviewapi.dto.response.UserDetailsResponse;
import br.com.emendes.yourreviewapi.model.Status;
import br.com.emendes.yourreviewapi.model.entity.User;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Classe para manter objetos fake relacionados a User para uso em testes automatizados.
 */
public class UserFaker {

  private UserFaker() {
  }

  /**
   * Retorna um User com os dados name, email e password (não encriptado).
   */
  public static User userToBeRegistered() {
    return User.builder()
        .name("Lorem Ipsum")
        .email("lorem@email.com")
        .password("1234567890")
        .build();
  }

  /**
   * Retorna um User com todos os dados.
   */
  public static User user() {
    return User.builder()
        .id(100L)
        .name("Lorem Ipsum")
        .email("lorem@email.com")
        .password("1234567890")
        .createdAt(LocalDateTime.parse("2023-12-17T10:00:00"))
        .status(Status.ENABLED)
        .authorities(Set.of(AuthorityFaker.userAuthority()))
        .build();
  }

  /**
   * Retorna um UserDetailsResponse com todos os campos.
   */
  public static UserDetailsResponse userDetailsResponse() {
    return UserDetailsResponse.builder()
        .id(100L)
        .name("Lorem Ipsum")
        .email("lorem@email.com")
        .createdAt(LocalDateTime.parse("2023-12-17T10:00:00"))
        .status("ENABLED")
        .build();
  }

}
