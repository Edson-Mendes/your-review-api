package br.com.emendes.yourreviewapi.mapper.impl;

import br.com.emendes.yourreviewapi.dto.request.UserRegisterRequest;
import br.com.emendes.yourreviewapi.dto.response.UserDetailsResponse;
import br.com.emendes.yourreviewapi.mapper.UserMapper;
import br.com.emendes.yourreviewapi.model.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Implementação de {@link UserMapper}.
 */
@Component
public class UserMapperImpl implements UserMapper {

  @Override
  public User toUser(UserRegisterRequest userRegisterRequest) {
    Assert.notNull(userRegisterRequest, "userRegisterRequest must not be null");

    return User.builder()
        .name(userRegisterRequest.name())
        .email(userRegisterRequest.email())
        .password(userRegisterRequest.password())
        .build();
  }

  @Override
  public UserDetailsResponse toUserDetailsResponse(User user) {
    Assert.notNull(user, "user must not be null");

    return UserDetailsResponse.builder()
        .id(user.getId())
        .name(user.getName())
        .email(user.getEmail())
        .status(user.getStatus().name())
        .createdAt(user.getCreatedAt())
        .build();
  }

}
