package br.com.emendes.yourreviewapi.integration.security;

import br.com.emendes.yourreviewapi.config.security.SecurityConfig;
import br.com.emendes.yourreviewapi.controller.MovieController;
import br.com.emendes.yourreviewapi.service.JWTService;
import br.com.emendes.yourreviewapi.service.MovieService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = {MovieController.class},
    includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)},
    excludeAutoConfiguration = {RedisAutoConfiguration.class}
)
@DisplayName("Integration tests for security layer in Authentication endpoints")
class MovieSecurityIT {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private JWTService jwtServiceMock;
  @MockBean
  private MovieService movieServiceMock;
  @MockBean
  private UserDetailsService userDetailsServiceMock;

  private final String CONTENT_TYPE = "application/json;charset=UTF-8";

  @Nested
  @DisplayName("Integration tests for security in findByName endpoint")
  class FindByNameEndpoint {

    private final String MOVIE_BY_NAME_URI = "/api/v1/movies";

    @Test
    @DisplayName("findByName must return status 200 when user is authenticated")
    void findByName_MustReturnStatus200_WhenUserIsAuthenticated() throws Exception {
      when(jwtServiceMock.isTokenValid(any())).thenReturn(true);
      when(jwtServiceMock.extractSubject("thisIsA.mock.JWT")).thenReturn("john.doe@email.com");
      when(userDetailsServiceMock.loadUserByUsername("john.doe@email.com")).thenReturn(UserFaker.user());

      mockMvc.perform(get(MOVIE_BY_NAME_URI)
              .param("name", "XPTO")
              .contentType(CONTENT_TYPE)
              .header("authorization", "Bearer thisIsA.mock.JWT"))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("findByName must return status 200 when user is not authenticated")
    void findByName_MustReturnStatus200_WhenUserIsNotAuthenticated() throws Exception {
      mockMvc.perform(get(MOVIE_BY_NAME_URI).param("name", "XPTO").contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }

  }

  @Nested
  @DisplayName("Integration tests for security in findById endpoint")
  class FindByIdEndpoint {

    private final String MOVIE_BY_ID_URI = "/api/v1/movies/{movieId}";

    @Test
    @DisplayName("findById must return status 200 when user is authenticated")
    void findById_MustReturnStatus200_WhenUserIsAuthenticated() throws Exception {
      when(jwtServiceMock.isTokenValid(any())).thenReturn(true);
      when(jwtServiceMock.extractSubject("thisIsA.mock.JWT")).thenReturn("john.doe@email.com");
      when(userDetailsServiceMock.loadUserByUsername("john.doe@email.com")).thenReturn(UserFaker.user());

      mockMvc.perform(get(MOVIE_BY_ID_URI, "1234")
              .contentType(CONTENT_TYPE)
              .header("authorization", "Bearer thisIsA.mock.JWT"))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("findById must return status 200 when user is not authenticated")
    void findById_MustReturnStatus200_WhenUserIsNotAuthenticated() throws Exception {
      mockMvc.perform(get(MOVIE_BY_ID_URI, "1234").contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }

  }

}
