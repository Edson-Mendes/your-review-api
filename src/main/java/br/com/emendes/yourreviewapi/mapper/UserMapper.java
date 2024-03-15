package br.com.emendes.yourreviewapi.mapper;

import br.com.emendes.yourreviewapi.dto.request.UserRegisterRequest;
import br.com.emendes.yourreviewapi.dto.response.UserDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.UserSummaryResponse;
import br.com.emendes.yourreviewapi.model.entity.User;
import br.com.emendes.yourreviewapi.repository.projection.UserSummaryProjection;

/**
 * Interface com as abstrações para mapeamento do recurso User.
 */
public interface UserMapper {

  /**
   * Mapeia um objeto UserRegisterRequest para User.
   *
   * @param userRegisterRequest objeto a ser mapeado para User.
   * @return Objeto User com os dados de UserRegisterRequest.
   * @throws IllegalArgumentException caso userRegisterRequest seja null.
   */
  User toUser(UserRegisterRequest userRegisterRequest);

  /**
   * Mapeia um objeto User para UserSummaryResponse.
   *
   * @param user objeto a ser mapeado para UserSummaryResponse.
   * @return Objeto UserSummaryResponse com os dados de User.
   * @throws IllegalArgumentException caso user seja null.
   */
  UserDetailsResponse toUserDetailsResponse(User user);

  /**
   * Mapeia um objeto {@link UserSummaryProjection} em {@link UserSummaryResponse}.
   *
   * @param userSummaryProjection Objeto a ser mapeado.
   * @return UserSummaryResponse com as informações de UserSummaryProjection.
   * @throws IllegalArgumentException caso userSummaryProjection seja null.
   */
  UserSummaryResponse toUserSummaryResponse(UserSummaryProjection userSummaryProjection);

}
