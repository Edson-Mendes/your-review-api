package br.com.emendes.yourreviewapi.integration.security;

import br.com.emendes.yourreviewapi.config.security.SecurityConfig;
import br.com.emendes.yourreviewapi.controller.AuthenticationController;
import br.com.emendes.yourreviewapi.service.AuthenticationService;
import br.com.emendes.yourreviewapi.service.JWTService;
import br.com.emendes.yourreviewapi.util.faker.UserFaker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = {AuthenticationController.class},
    includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)},
    excludeAutoConfiguration = {RedisAutoConfiguration.class}
)
@DisplayName("Integration tests for security layer in Authentication endpoints")
class AuthenticationSecurityIT {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private JWTService jwtServiceMock;
  @MockBean
  private AuthenticationService authenticationServiceMock;
  @MockBean
  private UserDetailsService userDetailsServiceMock;

  private final String CONTENT_TYPE = "application/json;charset=UTF-8";


  @Nested
  @DisplayName("Integration tests for security in signIn endpoint")
  class SignInEndpoint {

    private final String SIGN_IN_URI = "/api/v1/auth/signin";

    @Test
    @DisplayName("signIn must return status 200 when user is authenticated")
    void signIn_MustReturnStatus200_WhenUserIsAuthenticated() throws Exception {
      when(jwtServiceMock.isTokenValid(any())).thenReturn(true);
      when(jwtServiceMock.extractSubject("thisIsA.mock.JWT")).thenReturn("john.doe@email.com");
      when(userDetailsServiceMock.loadUserByUsername("john.doe@email.com")).thenReturn(UserFaker.user());

      String requestBody = """
          {
            "username": "lorem@email.com",
            "password": "1234567890"
          }
          """;

      mockMvc.perform(post(SIGN_IN_URI)
              .contentType(CONTENT_TYPE)
              .header("authorization", "Bearer thisIsA.mock.JWT")
              .content(requestBody))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("signIn must return status 200 when user is not authenticated")
    void signIn_MustReturnStatus200_WhenUserIsNotAuthenticated() throws Exception {
      String requestBody = """
          {
            "username": "lorem@email.com",
            "password": "1234567890"
          }
          """;

      mockMvc.perform(post(SIGN_IN_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isOk());
    }

  }

  @Nested
  @DisplayName("Integration tests for security in refreshToken endpoint")
  class RefreshTokenEndpoint {

    private final String REFRESH_TOKEN_URI = "/api/v1/auth/refresh";

    @Test
    @DisplayName("refreshToken must return status 200 when user is authenticated")
    void refreshToken_MustReturnStatus200_WhenUserIsAuthenticated() throws Exception {
      when(jwtServiceMock.isTokenValid(any())).thenReturn(true);
      when(jwtServiceMock.extractSubject("thisIsA.mock.JWT")).thenReturn("john.doe@email.com");
      when(userDetailsServiceMock.loadUserByUsername("john.doe@email.com")).thenReturn(UserFaker.user());

      mockMvc.perform(get(REFRESH_TOKEN_URI)
              .contentType(CONTENT_TYPE)
              .header("authorization", "Bearer thisIsA.mock.JWT"))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("refreshToken must return status 401 when user is not authenticated")
    void refreshToken_MustReturnStatus401_WhenUserIsNotAuthenticated() throws Exception {
      mockMvc.perform(get(REFRESH_TOKEN_URI)
              .contentType(CONTENT_TYPE))
          .andExpect(status().isUnauthorized());
    }

  }

}
