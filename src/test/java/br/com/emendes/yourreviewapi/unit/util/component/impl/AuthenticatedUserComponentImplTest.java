package br.com.emendes.yourreviewapi.unit.util.component.impl;

import br.com.emendes.yourreviewapi.exception.UserIsNotAuthenticatedException;
import br.com.emendes.yourreviewapi.model.entity.User;
import br.com.emendes.yourreviewapi.util.component.impl.AuthenticatedUserComponentImpl;
import br.com.emendes.yourreviewapi.util.faker.UserFaker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests de AuthenticatedUserComponentImpl")
class AuthenticatedUserComponentImplTest {

  @InjectMocks
  private AuthenticatedUserComponentImpl authenticatedUserComponent;
  @Mock
  private SecurityContext securityContextMock;
  @Mock
  private Authentication authenticationMock;

  @BeforeEach
  void setUp() {
    SecurityContextHolder.setContext(securityContextMock);
  }

  @Test
  @DisplayName("getCurrentUser must return User when get successfully")
  void getCurrentUser_MustReturnUser_WhenGetSuccessfully() {
    when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
    when(authenticationMock.isAuthenticated()).thenReturn(true);
    when(authenticationMock.getPrincipal()).thenReturn(UserFaker.user());

    User actualCurrentUser = authenticatedUserComponent.getCurrentUser();

    assertThat(actualCurrentUser).isNotNull();
    assertThat(actualCurrentUser.getId()).isNotNull().isEqualTo(100L);
  }

  @Test
  @DisplayName("getCurrentUser must throw UserIsNotAuthenticatedException when authentication is null")
  void getCurrentUser_MustThrowUserIsNotAuthenticatedException_WhenAuthenticationIsNull() {
    when(securityContextMock.getAuthentication()).thenReturn(null);

    Assertions.assertThatExceptionOfType(UserIsNotAuthenticatedException.class)
        .isThrownBy(() -> authenticatedUserComponent.getCurrentUser())
        .withMessage("User is not authenticate");
  }

  @Test
  @DisplayName("getCurrentUser must throw UserIsNotAuthenticatedException when is not authenticated")
  void getCurrentUser_MustThrowUserIsNotAuthenticatedException_WhenIsNotAuthenticated() {
    when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);

    Assertions.assertThatExceptionOfType(UserIsNotAuthenticatedException.class)
        .isThrownBy(() -> authenticatedUserComponent.getCurrentUser())
        .withMessage("User is not authenticate");
  }

  @Test
  @DisplayName("getCurrentUser must throw UserIsNotAuthenticatedException when user is anonymous user")
  void getCurrentUser_MustThrowUserIsNotAuthenticatedException_WhenUserIsAnonymousUser() {
    when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
    when(authenticationMock.isAuthenticated()).thenReturn(true);
    when(authenticationMock.getPrincipal()).thenReturn("AnonymousUser");

    Assertions.assertThatExceptionOfType(UserIsNotAuthenticatedException.class)
        .isThrownBy(() -> authenticatedUserComponent.getCurrentUser())
        .withMessage("User is not authenticate");
  }

}