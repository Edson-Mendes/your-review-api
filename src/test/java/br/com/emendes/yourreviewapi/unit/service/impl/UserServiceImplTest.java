package br.com.emendes.yourreviewapi.unit.service.impl;

import br.com.emendes.yourreviewapi.dto.request.UserRegisterRequest;
import br.com.emendes.yourreviewapi.dto.response.UserDetailsResponse;
import br.com.emendes.yourreviewapi.exception.EmailAlreadyInUseException;
import br.com.emendes.yourreviewapi.exception.PasswordsDoesNotMatchException;
import br.com.emendes.yourreviewapi.mapper.UserMapper;
import br.com.emendes.yourreviewapi.repository.UserRepository;
import br.com.emendes.yourreviewapi.service.AuthorityService;
import br.com.emendes.yourreviewapi.service.impl.UserServiceImpl;
import br.com.emendes.yourreviewapi.util.faker.AuthorityFaker;
import br.com.emendes.yourreviewapi.util.faker.UserFaker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests de UserServiceImpl")
class UserServiceImplTest {

  @InjectMocks
  private UserServiceImpl userService;
  @Mock
  private UserMapper userMapperMock;
  @Mock
  private AuthorityService authorityServiceMock;
  @Mock
  private PasswordEncoder passwordEncoderMock;
  @Mock
  private UserRepository userRepositoryMock;

  @Nested
  @DisplayName("register method")
  class RegisterMethod {

    @Test
    @DisplayName("register must return UserDetailsResponse when register successfully")
    void register_MustReturnUserDetailsResponse_WhenRegisterSuccessfully() {
      when(userMapperMock.toUser(any())).thenReturn(UserFaker.userToBeRegistered());
      when(authorityServiceMock.findByName(any())).thenReturn(AuthorityFaker.userAuthority());
      when(passwordEncoderMock.encode(any())).thenReturn("{bcryp}1234567890");
      when(userRepositoryMock.save(any())).thenReturn(UserFaker.user());
      when(userMapperMock.toUserDetailsResponse(any())).thenReturn(UserFaker.userDetailsResponse());

      UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
          .name("John Doe")
          .email("john.doe@email.com")
          .password("1234567890")
          .confirmPassword("1234567890")
          .build();

      UserDetailsResponse actualUserDetailsResponse = userService.register(userRegisterRequest);

      assertThat(actualUserDetailsResponse).isNotNull();
      assertThat(actualUserDetailsResponse.id()).isNotNull();
      assertThat(actualUserDetailsResponse.name()).isNotNull().isEqualTo("John Doe");
      assertThat(actualUserDetailsResponse.email()).isNotNull().isEqualTo("john.doe@email.com");
      assertThat(actualUserDetailsResponse.status()).isNotNull().isEqualTo("ENABLED");
      assertThat(actualUserDetailsResponse.createdAt()).isNotNull();
    }

    @Test
    @DisplayName("register must throw PasswordsDoesNotMatchException when passwords does not match")
    void register_MustThrowPasswordsDoesNotMatchException_WhenPasswordsDoesNotMatch() {
      UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
          .name("John Doe")
          .email("john.doe@email.com")
          .password("1234567890")
          .confirmPassword("123456789_")
          .build();

      assertThatExceptionOfType(PasswordsDoesNotMatchException.class)
          .isThrownBy(() -> userService.register(userRegisterRequest))
          .withMessage("passwords does not match");
    }

    @Test
    @DisplayName("register must throw EmailAlreadyInUseException when email already in use")
    void register_MustThrowEmailAlreadyInUseException_WhenEmailAlreadyInUse() {
      when(userMapperMock.toUser(any())).thenReturn(UserFaker.userToBeRegistered());
      when(authorityServiceMock.findByName(any())).thenReturn(AuthorityFaker.userAuthority());
      when(passwordEncoderMock.encode(any())).thenReturn("{bcryp}1234567890");
      given(userRepositoryMock.save(any()))
          .willThrow(new DataIntegrityViolationException("Database constraint violation"));

      UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
          .name("John Doe")
          .email("john.doe@email.com")
          .password("1234567890")
          .confirmPassword("1234567890")
          .build();

      assertThatExceptionOfType(EmailAlreadyInUseException.class)
          .isThrownBy(() -> userService.register(userRegisterRequest))
          .withMessage("Email {john.doe@email.com} already in use");
    }

  }

}