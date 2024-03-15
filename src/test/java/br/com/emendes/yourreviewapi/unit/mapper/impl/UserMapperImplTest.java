package br.com.emendes.yourreviewapi.unit.mapper.impl;

import br.com.emendes.yourreviewapi.dto.request.UserRegisterRequest;
import br.com.emendes.yourreviewapi.dto.response.UserDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.UserSummaryResponse;
import br.com.emendes.yourreviewapi.mapper.impl.UserMapperImpl;
import br.com.emendes.yourreviewapi.model.Status;
import br.com.emendes.yourreviewapi.model.entity.User;
import br.com.emendes.yourreviewapi.repository.projection.UserSummaryProjection;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@DisplayName("Unit tests de UserMapperImpl")
class UserMapperImplTest {

  private final UserMapperImpl userMapper = new UserMapperImpl();

  @Test
  @DisplayName("toUser must return User when userRegisterRequest is not null")
  void toUser_MustReturnUser_WhenUserRegisterRequestIsNotNull() {
    UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
        .name("John Doe")
        .email("john.doe@email.com")
        .password("1234567890")
        .build();

    User actualUser = userMapper.toUser(userRegisterRequest);

    Assertions.assertThat(actualUser).isNotNull();
    Assertions.assertThat(actualUser.getName()).isNotNull().isEqualTo("John Doe");
    Assertions.assertThat(actualUser.getEmail()).isNotNull().isEqualTo("john.doe@email.com");
    Assertions.assertThat(actualUser.getPassword()).isNotNull().isEqualTo("1234567890");
  }

  @Test
  @DisplayName("toUser must throw IllegalArgumentException when userRegisterRequest is null")
  void toUser_MustThrowIllegalArgumentException_WhenUserRegisterRequestIsNull() {
    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> userMapper.toUser(null))
        .withMessage("userRegisterRequest must not be null");
  }

  @Test
  @DisplayName("toUserDetailsResponse must return UserDetailsResponse when user is not null")
  void toUserDetailsResponse_MustReturnUserDetailsResponse_WhenUserIsNotNull() {
    User user = User.builder()
        .id(100L)
        .name("John Doe")
        .email("john.doe@email.com")
        .password("1234567890")
        .status(Status.ENABLED)
        .createdAt(LocalDateTime.parse("2023-12-16T10:00:00"))
        .build();

    UserDetailsResponse actualUserDetailsResponse = userMapper.toUserDetailsResponse(user);

    Assertions.assertThat(actualUserDetailsResponse).isNotNull();
    Assertions.assertThat(actualUserDetailsResponse.id()).isNotNull().isEqualTo(100L);
    Assertions.assertThat(actualUserDetailsResponse.name()).isNotNull().isEqualTo("John Doe");
    Assertions.assertThat(actualUserDetailsResponse.email()).isNotNull().isEqualTo("john.doe@email.com");
    Assertions.assertThat(actualUserDetailsResponse.status()).isNotNull().isEqualTo("ENABLED");
    Assertions.assertThat(actualUserDetailsResponse.createdAt()).isNotNull()
        .isEqualTo("2023-12-16T10:00:00");
  }

  @Test
  @DisplayName("toUserDetailsResponse must throw IllegalArgumentException when user is null")
  void toUserDetailsResponse_MustThrowIllegalArgumentException_WhenUserIsNull() {
    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> userMapper.toUserDetailsResponse(null))
        .withMessage("user must not be null");
  }

  @Test
  @DisplayName("toUserSummaryResponse must return UserDetailsResponse when map successfully")
  void toUserSummaryResponse_MustReturnUserDetailsResponse_WhenMapSuccessfully() {
    UserSummaryProjection userSummaryProjection = UserSummaryProjection.builder()
        .id(100L)
        .name("John Doe")
        .email("john.doe@email.com")
        .build();

    UserSummaryResponse actualUserSummaryResponse = userMapper.toUserSummaryResponse(userSummaryProjection);

    Assertions.assertThat(actualUserSummaryResponse).isNotNull();
    Assertions.assertThat(actualUserSummaryResponse.id()).isNotNull().isEqualTo(100L);
    Assertions.assertThat(actualUserSummaryResponse.name()).isNotNull().isEqualTo("John Doe");
    Assertions.assertThat(actualUserSummaryResponse.email()).isNotNull().isEqualTo("john.doe@email.com");
  }

  @Test
  @DisplayName("toUserSummaryResponse must throw IllegalArgumentException when userSummaryProjection is null")
  void toUserSummaryResponse_MustThrowIllegalArgumentException_WhenUserSummaryProjectionIsNull() {
    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> userMapper.toUserSummaryResponse(null))
        .withMessage("userSummaryProjection must not be null");
  }

}