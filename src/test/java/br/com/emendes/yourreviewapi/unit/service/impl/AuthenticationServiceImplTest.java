package br.com.emendes.yourreviewapi.unit.service.impl;

import br.com.emendes.yourreviewapi.dto.request.AuthenticationRequest;
import br.com.emendes.yourreviewapi.dto.response.AuthenticationResponse;
import br.com.emendes.yourreviewapi.exception.UserIsNotAuthenticatedException;
import br.com.emendes.yourreviewapi.service.JWTService;
import br.com.emendes.yourreviewapi.service.impl.AuthenticationServiceImpl;
import br.com.emendes.yourreviewapi.util.component.AuthenticatedUserComponent;
import br.com.emendes.yourreviewapi.util.faker.UserFaker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests de AuthenticationServiceImpl")
class AuthenticationServiceImplTest {

  @InjectMocks
  private AuthenticationServiceImpl authenticationService;
  @Mock
  private AuthenticationManager authenticationManagerMock;
  @Mock
  private JWTService jwtServiceMock;

  @Mock
  private AuthenticatedUserComponent authenticatedUserComponentMock;

  @Nested
  @DisplayName("authenticate method")
  class AuthenticateMethod {

    @Mock
    private Authentication authentication;

    @Test
    @DisplayName("authenticate must return AuthenticationResponse when authenticate successfully")
    void authenticate_MustReturnAuthenticationResponse_WhenAuthenticateSuccessfully() {
      when(authenticationManagerMock.authenticate(any())).thenReturn(authentication);
      when(authentication.getPrincipal()).thenReturn(UserFaker.user());
      when(jwtServiceMock.generateToken(any(), anyLong())).thenReturn("token");

      AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
          .username("lorem@email.com")
          .password("1234567890")
          .build();

      AuthenticationResponse actualAuthenticationResponse = authenticationService.authenticate(authenticationRequest);

      assertThat(actualAuthenticationResponse).isNotNull();
      assertThat(actualAuthenticationResponse.type()).isNotNull().isEqualTo("Bearer");
      assertThat(actualAuthenticationResponse.token()).isNotNull().isNotBlank();
    }

    @Test
    @DisplayName("authenticate must throw BadCredentialsException when credentials are invalid")
    void authenticate_MustThrowBadCredentialsException_WhenCredentialsAreInvalid() {
      given(authenticationManagerMock.authenticate(any())).willThrow(new BadCredentialsException("Bad credentials"));

      AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
          .username("lorem@email.com")
          .password("123456789_")
          .build();

      assertThatExceptionOfType(BadCredentialsException.class)
          .isThrownBy(() -> authenticationService.authenticate(authenticationRequest))
          .withMessage("Bad credentials");
    }

    @Test
    @DisplayName("authenticate must throw LockedException when user is blocked")
    void authenticate_MustThrowLockedException_WhenUserIsBlocked() {
      given(authenticationManagerMock.authenticate(any())).willThrow(new LockedException("User account is locked"));

      AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
          .username("lorem@email.com")
          .password("123456789_")
          .build();

      assertThatExceptionOfType(LockedException.class)
          .isThrownBy(() -> authenticationService.authenticate(authenticationRequest))
          .withMessage("User account is locked");
    }

    @Test
    @DisplayName("authenticate must throw DisabledException when user is blocked")
    void authenticate_MustThrowDisabledException_WhenUserIsBlocked() {
      given(authenticationManagerMock.authenticate(any())).willThrow(new DisabledException("User is disabled"));

      AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
          .username("lorem@email.com")
          .password("123456789_")
          .build();

      assertThatExceptionOfType(DisabledException.class)
          .isThrownBy(() -> authenticationService.authenticate(authenticationRequest))
          .withMessage("User is disabled");
    }

  }

  @Nested
  @DisplayName("refreshToken method")
  class RefreshTokenMethod {

    @Test
    @DisplayName("refreshToken must return AuthenticationResponse when refresh token successfully")
    void refreshToken_MustReturnAuthenticationResponse_WhenRefreshTokenSuccessfully() {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(jwtServiceMock.generateToken(any(), anyLong())).thenReturn("refreshed.token");

      AuthenticationResponse actualAuthenticationResponse = authenticationService.refreshToken();

      assertThat(actualAuthenticationResponse).isNotNull();
      assertThat(actualAuthenticationResponse.type()).isNotNull().isEqualTo("Bearer");
      assertThat(actualAuthenticationResponse.token()).isNotNull().isEqualTo("refreshed.token");
    }

    @Test
    @DisplayName("refreshToken must throw UserIsNotAuthenticatedException when user is not authenticated")
    void refreshToken_MustThrowUserIsNotAuthenticatedException_WhenUserIsNotAuthenticated() {
      when(authenticatedUserComponentMock.getCurrentUser())
          .thenThrow(new UserIsNotAuthenticatedException("User is not authenticate"));

      assertThatExceptionOfType(UserIsNotAuthenticatedException.class)
          .isThrownBy(() -> authenticationService.refreshToken())
          .withMessage("User is not authenticate");
    }

  }

}