package br.com.emendes.yourreviewapi.integration.security;

import br.com.emendes.yourreviewapi.config.security.SecurityConfig;
import br.com.emendes.yourreviewapi.controller.ReviewController;
import br.com.emendes.yourreviewapi.service.JWTService;
import br.com.emendes.yourreviewapi.service.ReviewService;
import br.com.emendes.yourreviewapi.util.faker.ReviewFaker;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = {ReviewController.class},
    includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)},
    excludeAutoConfiguration = {RedisAutoConfiguration.class}
)
@DisplayName("Integration tests for security layer in Authentication endpoints")
class ReviewSecurityIT {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private JWTService jwtServiceMock;
  @MockBean
  private ReviewService reviewServiceMock;
  @MockBean
  private UserDetailsService userDetailsServiceMock;

  private final String CONTENT_TYPE = "application/json;charset=UTF-8";

  @Nested
  @DisplayName("Integration tests for security in register endpoint")
  class RegisterEndpoint {

    private final String REVIEW_REGISTER_URI = "/api/v1/reviews";

    @Test
    @DisplayName("register must return status 201 when user is authenticated")
    void register_MustReturnStatus201_WhenUserIsAuthenticated() throws Exception {
      when(jwtServiceMock.isTokenValid(any())).thenReturn(true);
      when(jwtServiceMock.extractSubject("thisIsA.mock.JWT")).thenReturn("john.doe@email.com");
      when(userDetailsServiceMock.loadUserByUsername("john.doe@email.com")).thenReturn(UserFaker.user());
      when(reviewServiceMock.register(any())).thenReturn(ReviewFaker.reviewResponse());

      String requestBody = """
          {
            "vote": 9,
            "opinion": "Lorem ipsum dolor sit amet",
            "movieId": "1234"
          }
          """;

      mockMvc.perform(post(REVIEW_REGISTER_URI)
              .content(requestBody)
              .contentType(CONTENT_TYPE)
              .header("authorization", "Bearer thisIsA.mock.JWT"))
          .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("register must return status 401 when user is not authenticated")
    void register_MustReturnStatus401_WhenUserIsNotAuthenticated() throws Exception {
      String requestBody = """
          {
            "vote": 9,
            "opinion": "Lorem ipsum dolor sit amet",
            "movieId": "1234"
          }
          """;

      mockMvc.perform(post(REVIEW_REGISTER_URI)
              .content(requestBody)
              .contentType(CONTENT_TYPE))
          .andExpect(status().isUnauthorized());
    }

  }

  @Nested
  @DisplayName("Integration tests for security in fetchByMovieId endpoint")
  class FetchByMovieIdEndpoint {

    private final String REVIEW_BY_MOVIE_ID_URI = "/api/v1/reviews";

    @Test
    @DisplayName("fetchByMovieId must return status 200 when user is authenticated")
    void fetchByMovieId_MustReturnStatus200_WhenUserIsAuthenticated() throws Exception {
      when(jwtServiceMock.isTokenValid(any())).thenReturn(true);
      when(jwtServiceMock.extractSubject("thisIsA.mock.JWT")).thenReturn("john.doe@email.com");
      when(userDetailsServiceMock.loadUserByUsername("john.doe@email.com")).thenReturn(UserFaker.user());

      mockMvc.perform(get(REVIEW_BY_MOVIE_ID_URI)
              .param("movieId", "1234")
              .param("page", "2")
              .contentType(CONTENT_TYPE)
              .header("authorization", "Bearer thisIsA.mock.JWT"))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("fetchByMovieId must return status 401 when user is not authenticated")
    void fetchByMovieId_MustReturnStatus401_WhenUserIsNotAuthenticated() throws Exception {
      mockMvc.perform(get(REVIEW_BY_MOVIE_ID_URI)
              .param("movieId", "1234")
              .param("page", "2")
              .contentType(CONTENT_TYPE))
          .andExpect(status().isUnauthorized());
    }

  }

  @Nested
  @DisplayName("Integration tests for security in findById endpoint")
  class FindByIdEndpoint {

    private final String REVIEW_BY_ID_URI = "/api/v1/reviews/{reviewId}";

    @Test
    @DisplayName("findById must return status 200 when user is authenticated")
    void findById_MustReturnStatus200_WhenUserIsAuthenticated() throws Exception {
      when(jwtServiceMock.isTokenValid(any())).thenReturn(true);
      when(jwtServiceMock.extractSubject("thisIsA.mock.JWT")).thenReturn("john.doe@email.com");
      when(userDetailsServiceMock.loadUserByUsername("john.doe@email.com")).thenReturn(UserFaker.user());

      mockMvc.perform(get(REVIEW_BY_ID_URI, "2000000")
              .contentType(CONTENT_TYPE)
              .header("authorization", "Bearer thisIsA.mock.JWT"))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("findById must return status 401 when user is not authenticated")
    void findById_MustReturnStatus401_WhenUserIsNotAuthenticated() throws Exception {
      mockMvc.perform(get(REVIEW_BY_ID_URI, "2000000")
              .contentType(CONTENT_TYPE))
          .andExpect(status().isUnauthorized());
    }

  }

  @Nested
  @DisplayName("Integration tests for security in updateById endpoint")
  class UpdateByIdEndpoint {

    private final String REVIEW_UPDATE_URI = "/api/v1/reviews/{reviewId}";

    @Test
    @DisplayName("updateById must return status 200 when user is authenticated")
    void updateById_MustReturnStatus200_WhenUserIsAuthenticated() throws Exception {
      when(jwtServiceMock.isTokenValid(any())).thenReturn(true);
      when(jwtServiceMock.extractSubject("thisIsA.mock.JWT")).thenReturn("john.doe@email.com");
      when(userDetailsServiceMock.loadUserByUsername("john.doe@email.com")).thenReturn(UserFaker.user());

      String requestBody = """
          {
            "vote": 8,
            "opinion": "Lorem ipsum dolor sit amet updated"
          }
          """;

      mockMvc.perform(put(REVIEW_UPDATE_URI, "2000000")
              .content(requestBody)
              .contentType(CONTENT_TYPE)
              .header("authorization", "Bearer thisIsA.mock.JWT"))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("updateById must return status 401 when user is not authenticated")
    void updateById_MustReturnStatus401_WhenUserIsNotAuthenticated() throws Exception {
      String requestBody = """
          {
            "vote": 8,
            "opinion": "Lorem ipsum dolor sit amet updated"
          }
          """;

      mockMvc.perform(put(REVIEW_UPDATE_URI, "2000000")
              .content(requestBody)
              .contentType(CONTENT_TYPE))
          .andExpect(status().isUnauthorized());
    }

  }

  @Nested
  @DisplayName("Integration tests for security in deleteById endpoint")
  class DeleteByIdEndpoint {

    private final String REVIEW_DELETE_BY_ID_URI = "/api/v1/reviews/{reviewId}";

    @Test
    @DisplayName("deleteById must return status 204 when user is authenticated")
    void deleteById_MustReturnStatus204_WhenUserIsAuthenticated() throws Exception {
      when(jwtServiceMock.isTokenValid(any())).thenReturn(true);
      when(jwtServiceMock.extractSubject("thisIsA.mock.JWT")).thenReturn("john.doe@email.com");
      when(userDetailsServiceMock.loadUserByUsername("john.doe@email.com")).thenReturn(UserFaker.user());

      mockMvc.perform(delete(REVIEW_DELETE_BY_ID_URI, "2000000")
              .contentType(CONTENT_TYPE)
              .header("authorization", "Bearer thisIsA.mock.JWT"))
          .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("deleteById must return status 401 when user is not authenticated")
    void deleteById_MustReturnStatus401_WhenUserIsNotAuthenticated() throws Exception {
      mockMvc.perform(delete(REVIEW_DELETE_BY_ID_URI, "2000000")
              .contentType(CONTENT_TYPE))
          .andExpect(status().isUnauthorized());
    }

  }

}
