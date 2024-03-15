package br.com.emendes.yourreviewapi.mapper.impl;

import br.com.emendes.yourreviewapi.dto.request.UserRegisterRequest;
import br.com.emendes.yourreviewapi.dto.response.UserDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.UserSummaryResponse;
import br.com.emendes.yourreviewapi.mapper.UserMapper;
import br.com.emendes.yourreviewapi.model.entity.User;
import br.com.emendes.yourreviewapi.repository.projection.UserSummaryProjection;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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

  @Override
  public UserSummaryResponse toUserSummaryResponse(UserSummaryProjection userSummaryProjection) {
    Assert.notNull(userSummaryProjection, "userSummaryProjection must not be null");

    return UserSummaryResponse.builder()
        .id(userSummaryProjection.id())
        .name(userSummaryProjection.name())
        .email(userSummaryProjection.email())
        .build();
  }

}
