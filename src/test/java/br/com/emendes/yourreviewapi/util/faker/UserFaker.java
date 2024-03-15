package br.com.emendes.yourreviewapi.util.faker;

import br.com.emendes.yourreviewapi.dto.response.UserDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.UserSummaryResponse;
import br.com.emendes.yourreviewapi.model.Status;
import br.com.emendes.yourreviewapi.model.entity.User;
import br.com.emendes.yourreviewapi.repository.projection.UserSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Classe para manter objetos fake relacionados a User para uso em testes automatizados.
 */
public class UserFaker {

  public static final long USER_ID = 100L;
  public static final String USER_NAME = "John Doe";
  public static final String USER_EMAIL = "john.doe@email.com";
  public static final String USER_PASSWORD = "1234567890";
  public static final LocalDateTime USER_CREATED_AT = LocalDateTime.parse("2023-12-17T10:00:00");

  private UserFaker() {
  }

  /**
   * Retorna um User com os dados name, email e password (não encriptado).
   */
  public static User userToBeRegistered() {
    return User.builder()
        .name(USER_NAME)
        .email(USER_EMAIL)
        .password(USER_PASSWORD)
        .build();
  }

  /**
   * Retorna um User com todos os dados.
   */
  public static User user() {
    return User.builder()
        .id(USER_ID)
        .name(USER_NAME)
        .email(USER_EMAIL)
        .password(USER_PASSWORD)
        .createdAt(USER_CREATED_AT)
        .status(Status.ENABLED)
        .authorities(Set.of(AuthorityFaker.userAuthority()))
        .build();
  }

  /**
   * Retorna um {@link User} com todos os dados e Authority ADMIN
   */
  public static User userAdmin() {
    return User.builder()
        .id(USER_ID)
        .name(USER_NAME)
        .email(USER_EMAIL)
        .password(USER_PASSWORD)
        .createdAt(USER_CREATED_AT)
        .status(Status.ENABLED)
        .authorities(Set.of(AuthorityFaker.adminAuthority(), AuthorityFaker.userAuthority()))
        .build();
  }

  /**
   * Retorna um UserDetailsResponse com todos os campos.
   */
  public static UserDetailsResponse userDetailsResponse() {
    return UserDetailsResponse.builder()
        .id(USER_ID)
        .name(USER_NAME)
        .email(USER_EMAIL)
        .createdAt(USER_CREATED_AT)
        .status("ENABLED")
        .build();
  }

  /**
   * Retorna um objeto UserSummaryResponse com todos os campos.
   */
  public static UserSummaryResponse userSummaryResponse() {
    return UserSummaryResponse.builder()
        .id(USER_ID)
        .name(USER_NAME)
        .email(USER_EMAIL)
        .build();
  }

  /**
   * Retorna uma instância de {@code Page<UserSummaryProjection>} contendo um {@link UserSummaryProjection}.
   */
  public static Page<UserSummaryProjection> userSummaryProjectionPage() {
    return new PageImpl<>(List.of(userSummaryProjection()), PageRequest.of(0, 20), 1);
  }

  /**
   * Retorna uma instância de {@link UserSummaryProjection} com todos os campos.
   */
  private static UserSummaryProjection userSummaryProjection() {
    return UserSummaryProjection.builder()
        .id(USER_ID)
        .name(USER_NAME)
        .email(USER_EMAIL)
        .build();
  }

  /**
   * Retorna uma instância de {@code Page<UserSummaryResponse>} contendo um {@link UserSummaryResponse}.
   */
  public static Page<UserSummaryResponse> userSummaryResponsePage() {
    return new PageImpl<>(List.of(userSummaryResponse()), PageRequest.of(0, 20), 1);
  }
}
