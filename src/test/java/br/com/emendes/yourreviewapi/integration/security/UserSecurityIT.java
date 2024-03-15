package br.com.emendes.yourreviewapi.integration.security;

import br.com.emendes.yourreviewapi.config.security.SecurityConfig;
import br.com.emendes.yourreviewapi.controller.UserController;
import br.com.emendes.yourreviewapi.service.JWTService;
import br.com.emendes.yourreviewapi.service.UserService;
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
    controllers = {UserController.class},
    includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)},
    excludeAutoConfiguration = {RedisAutoConfiguration.class}
)
@DisplayName("Integration tests for security layer in Authentication endpoints")
class UserSecurityIT {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private JWTService jwtServiceMock;
  @MockBean
  private UserService userServiceMock;
  @MockBean
  private UserDetailsService userDetailsServiceMock;

  private final String CONTENT_TYPE = "application/json;charset=UTF-8";

  @Nested
  @DisplayName("Integration tests for security in register endpoint")
  class RegisterEndpoint {

    private final String USER_REGISTER_URI = "/api/v1/users";

    @Test
    @DisplayName("register must return status 201 when user is authenticated")
    void register_MustReturnStatus201_WhenUserIsAuthenticated() throws Exception {
      when(jwtServiceMock.isTokenValid(any())).thenReturn(true);
      when(jwtServiceMock.extractSubject("thisIsA.mock.JWT")).thenReturn("john.doe@email.com");
      when(userDetailsServiceMock.loadUserByUsername("john.doe@email.com")).thenReturn(UserFaker.user());
      when(userServiceMock.register(any())).thenReturn(UserFaker.userDetailsResponse());

      String requestBody = """
          {
            "name": "John Doe",
            "email": "john.doe@email.com",
            "password": "1234567890",
            "confirmPassword": "1234567890"
          }
          """;

      mockMvc.perform(post(USER_REGISTER_URI)
              .content(requestBody)
              .contentType(CONTENT_TYPE)
              .header("authorization", "Bearer thisIsA.mock.JWT"))
          .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("register must return status 201 when user is not authenticated")
    void register_MustReturnStatus201_WhenUserIsNotAuthenticated() throws Exception {
      when(userServiceMock.register(any())).thenReturn(UserFaker.userDetailsResponse());

      String requestBody = """
          {
            "name": "John Doe",
            "email": "john.doe@email.com",
            "password": "1234567890",
            "confirmPassword": "1234567890"
          }
          """;

      mockMvc.perform(post(USER_REGISTER_URI)
              .content(requestBody)
              .contentType(CONTENT_TYPE))
          .andExpect(status().isCreated());
    }

  }

  @Nested
  @DisplayName("Integration tests for security in fetch endpoint")
  class FetchEndpoint {

    private final String FETCH_URI = "/api/v1/users";

    @Test
    @DisplayName("fetch must return status 200 when user is ADMIN")
    void fetch_MustReturnStatus200_WhenUserIsAdmin() throws Exception {
      when(jwtServiceMock.isTokenValid(any())).thenReturn(true);
      when(jwtServiceMock.extractSubject("thisIsA.mock.AdminJWT")).thenReturn("john.doe@email.com");
      when(userDetailsServiceMock.loadUserByUsername("john.doe@email.com")).thenReturn(UserFaker.userAdmin());
      when(userServiceMock.fetch(any())).thenReturn(UserFaker.userSummaryResponsePage());

      mockMvc.perform(get(FETCH_URI).header("authorization", "Bearer thisIsA.mock.AdminJWT"))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("fetch must return status 401 when user is not authenticated")
    void fetch_MustReturnStatus401_WhenUserIsNotAuthenticated() throws Exception {
      mockMvc.perform(get(FETCH_URI))
          .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("fetch must return status 403 when user is not ADMIN")
    void fetch_MustReturnStatus403_WhenUserIsNotAdmin() throws Exception {
      when(jwtServiceMock.isTokenValid(any())).thenReturn(true);
      when(jwtServiceMock.extractSubject("thisIsA.mock.UserJWT")).thenReturn("john.doe@email.com");
      when(userDetailsServiceMock.loadUserByUsername("john.doe@email.com")).thenReturn(UserFaker.user());

      mockMvc.perform(get(FETCH_URI).header("authorization", "Bearer thisIsA.mock.UserJWT"))
          .andExpect(status().isForbidden());
    }

  }

}
