package br.com.emendes.yourreviewapi.unit.filter;

import br.com.emendes.yourreviewapi.config.security.filter.JWTAuthenticationFilter;
import br.com.emendes.yourreviewapi.model.entity.User;
import br.com.emendes.yourreviewapi.service.JWTService;
import br.com.emendes.yourreviewapi.util.faker.UserFaker;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for JWTAuthenticationFilter")
class JWTAuthenticationFilterTest {

  @InjectMocks
  private JWTAuthenticationFilter jwtAuthenticationFilter;
  @Mock
  private JWTService jwtServiceMock;
  @Mock
  private UserDetailsService userDetailsServiceMock;
  @Mock
  private HttpServletRequest requestMock;
  @Mock
  private HttpServletResponse responseMock;
  @Mock
  private FilterChain filterChainMock;

  @Test
  @DisplayName("doFilterInternal must set Authentication into SecurityContext when authenticate successfully")
  void doFilterInternal_MustSetAuthenticationIntoSecurityContext_WhenAuthenticateSuccessfully() throws ServletException, IOException {
    when(requestMock.getHeader("Authorization")).thenReturn("Bearer thisIsA.Mock.token");
    when(jwtServiceMock.isTokenValid("thisIsA.Mock.token")).thenReturn(true);
    when(jwtServiceMock.extractSubject("thisIsA.Mock.token")).thenReturn("john.doe@email.com");
    when(userDetailsServiceMock.loadUserByUsername("john.doe@email.com")).thenReturn(UserFaker.user());

    jwtAuthenticationFilter.doFilter(requestMock, responseMock, filterChainMock);

    Authentication actualAuthentication = SecurityContextHolder.getContext().getAuthentication();

    assertThat(actualAuthentication).isNotNull();
    assertThat(actualAuthentication.isAuthenticated()).isTrue();
    assertThat(actualAuthentication.getPrincipal()).isNotNull().isInstanceOf(User.class);
  }

  @Test
  @DisplayName("doFilterInternal must do nothing when request does have Authorization header")
  void doFilterInternal_MustDoNothing_WhenRequestDoesHaveAuthorizationHeader() throws ServletException, IOException {
    when(requestMock.getHeader("Authorization")).thenReturn(null);

    jwtAuthenticationFilter.doFilter(requestMock, responseMock, filterChainMock);

    Authentication actualAuthentication = SecurityContextHolder.getContext().getAuthentication();

    assertThat(actualAuthentication).isNull();
  }

  @Test
  @DisplayName("doFilterInternal must do nothing when Authorization header does not start with Bearer")
  void doFilterInternal_MustDoNothing_WhenAuthorizationHeaderDoesNotStartWithBearer() throws ServletException, IOException {
    when(requestMock.getHeader("Authorization")).thenReturn("thisIsA.Mock.token");

    jwtAuthenticationFilter.doFilter(requestMock, responseMock, filterChainMock);

    Authentication actualAuthentication = SecurityContextHolder.getContext().getAuthentication();

    assertThat(actualAuthentication).isNull();
  }

  @Test
  @DisplayName("doFilterInternal must do nothing when token is invalid")
  void doFilterInternal_MustDoNothing_WhenTokenIsInvalid() throws ServletException, IOException {
    when(requestMock.getHeader("Authorization")).thenReturn("Bearer thisIsA.Invalid.token");
    when(jwtServiceMock.isTokenValid("thisIsA.Invalid.token")).thenReturn(false);

    jwtAuthenticationFilter.doFilter(requestMock, responseMock, filterChainMock);

    Authentication actualAuthentication = SecurityContextHolder.getContext().getAuthentication();

    assertThat(actualAuthentication).isNull();
  }

}