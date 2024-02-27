package br.com.emendes.yourreviewapi.integration.controller;

import br.com.emendes.yourreviewapi.client.MovieClient;
import br.com.emendes.yourreviewapi.controller.MovieController;
import br.com.emendes.yourreviewapi.dto.response.MovieDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.MovieSummaryResponse;
import br.com.emendes.yourreviewapi.exception.InvalidTMDbApiKeyException;
import br.com.emendes.yourreviewapi.exception.MovieNotFoundException;
import br.com.emendes.yourreviewapi.mapper.MovieMapper;
import br.com.emendes.yourreviewapi.service.MovieService;
import br.com.emendes.yourreviewapi.util.PageableResponse;
import br.com.emendes.yourreviewapi.util.faker.MovieFaker;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"controller-it"})
@WebMvcTest(
    excludeAutoConfiguration = {RedisAutoConfiguration.class, SecurityAutoConfiguration.class},
    useDefaultFilters = false,
    includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
            MovieController.class,
            MovieService.class
        }),
        @ComponentScan.Filter(classes = {RestControllerAdvice.class})
    }
)
@DisplayName("Test dos endpoints de MovieController")
public class MovieControllerIT {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper mapper;
  @MockBean
  private MovieClient movieClientMock;
  @MockBean
  private MovieMapper movieMapperMock;

  private final String CONTENT_TYPE = "application/json;charset=UTF-8";

  @Nested
  @DisplayName("Tests for findByName endpoint")
  class FindByNameEndpoint {

    private static final String FIND_BY_NAME_URI = "/api/v1/movies";

    @Test
    @DisplayName("GET /api/v1/auth/movies must return status 200 when find by name successfully")
    void findByName_MustReturnStatus200_WhenFindByNameSuccessfully() throws Exception {
      when(movieClientMock.findByName(any(), anyInt())).thenReturn(MovieFaker.moviePage());
      when(movieMapperMock.toMovieSummaryResponse(any())).thenReturn(MovieFaker.movieSummaryResponse());

      mockMvc.perform(get(FIND_BY_NAME_URI).param("name", "Lorem").contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/auth/movies must return Page<MovieSummaryResponse> when find by name successfully")
    void findByName_MustReturnPageMovieSummaryResponse_WhenFindByNameSuccessfully() throws Exception {
      when(movieClientMock.findByName(any(), anyInt())).thenReturn(MovieFaker.moviePage());
      when(movieMapperMock.toMovieSummaryResponse(any())).thenReturn(MovieFaker.movieSummaryResponse());

      MovieSummaryResponse expectedMovieSummaryResponse = MovieSummaryResponse.builder()
          .id("1000000")
          .title("Lorem")
          .posterPath("/1000000")
          .releaseDate(LocalDate.parse("2024-01-16"))
          .build();

      String actualContent = mockMvc.perform(get(FIND_BY_NAME_URI).param("name", "Lorem").contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();

      Page<MovieSummaryResponse> actualResponseBody = mapper
          .readValue(actualContent, new TypeReference<PageableResponse<MovieSummaryResponse>>() {
          });

      assertThat(actualResponseBody).isNotNull().isNotEmpty().hasSize(1);
      List<MovieSummaryResponse> actualPageContent = actualResponseBody.getContent();
      assertThat(actualPageContent).isNotNull().containsExactly(expectedMovieSummaryResponse);
    }

    @Test
    @DisplayName("GET /api/v1/auth/movies must return status 200 when not found movie for given movie name")
    void findByName_MustReturnStatus200_WhenNotFoundMovieForGivenName() throws Exception {
      when(movieClientMock.findByName(any(), anyInt())).thenReturn(MovieFaker.emptyPage());

      mockMvc.perform(get(FIND_BY_NAME_URI).param("name", "Lorem").contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/auth/movies must return Page<MovieSummaryResponse> when not found movie for given movie name")
    void findByName_MustReturnPageMovieSummaryResponse_WhenNotFoundMovieForGivenName() throws Exception {
      when(movieClientMock.findByName(any(), anyInt())).thenReturn(MovieFaker.emptyPage());

      String actualContent = mockMvc.perform(get(FIND_BY_NAME_URI).param("name", "Lorem").contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();

      Page<MovieSummaryResponse> actualResponseBody = mapper
          .readValue(actualContent, new TypeReference<PageableResponse<MovieSummaryResponse>>() {
          });

      assertThat(actualResponseBody).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("GET /api/v1/auth/movies must return status 400 when request param name is blank")
    void findByName_MustReturnStatus400_WhenRequestParamNameIsBlank() throws Exception {
      mockMvc.perform(get(FIND_BY_NAME_URI).param("name", "").contentType(CONTENT_TYPE))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/v1/auth/movies must return ProblemDetail when request param name is blank")
    void findByName_MustReturnProblemDetail_WhenRequestParamNameIsBlank() throws Exception {
      String actualContent = mockMvc.perform(get(FIND_BY_NAME_URI).param("name", "").contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getStatus()).isEqualTo(400);
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Bad request");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("Some fields are invalids");
      assertThat(actualResponseBody.getProperties()).isNotNull();
      assertThat(actualResponseBody.getProperties().get("fields")).isNotNull().isEqualTo("name");
      assertThat(actualResponseBody.getProperties().get("messages")).isNotNull()
          .isEqualTo("movie name must not be blank");
    }

    @Test
    @DisplayName("GET /api/v1/auth/movies must return status 400 when request param page is negative")
    void findByName_MustReturnStatus400_WhenRequestParamPageIsNegative() throws Exception {
      mockMvc.perform(get(FIND_BY_NAME_URI)
              .param("name", "Lorem")
              .param("page", "-1")
              .contentType(CONTENT_TYPE))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/v1/auth/movies must return ProblemDetail when request param page is negative")
    void findByName_MustReturnProblemDetail_WhenRequestParamPageIsNegative() throws Exception {
      String actualContent = mockMvc.perform(get(FIND_BY_NAME_URI)
              .param("name", "Lorem")
              .param("page", "-1")
              .contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getStatus()).isEqualTo(400);
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Bad request");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("Some fields are invalids");
      assertThat(actualResponseBody.getProperties()).isNotNull();
      assertThat(actualResponseBody.getProperties().get("fields")).isNotNull().isEqualTo("page");
      assertThat(actualResponseBody.getProperties().get("messages")).isNotNull()
          .isEqualTo("page must be positive or zero");
    }

    @Test
    @DisplayName("GET /api/v1/auth/movies must return status 500 when api_key is invalid")
    void findByName_MustReturnStatus400_WhenAPIKEYIsInvalid() throws Exception {
      when(movieClientMock.findByName("Lorem", 0))
          .thenThrow(new InvalidTMDbApiKeyException("invalid TMDb API Key"));

      mockMvc.perform(get(FIND_BY_NAME_URI).param("name", "Lorem").contentType(CONTENT_TYPE))
          .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GET /api/v1/auth/movies must return ProblemDetail when api_key is invalid")
    void findByName_MustReturnProblemDetail_WhenAPIKEYIsInvalid() throws Exception {
      when(movieClientMock.findByName("Lorem", 0))
          .thenThrow(new InvalidTMDbApiKeyException("invalid TMDb API Key"));

      String actualContent = mockMvc.perform(get(FIND_BY_NAME_URI).param("name", "Lorem").contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getStatus()).isEqualTo(500);
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Internal server error");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("invalid TMDb API Key");
    }

  }

  @Nested
  @DisplayName("Tests for findById endpoint")
  class FindByIdEndpoint {

    private static final String FIND_BY_ID_URI = "/api/v1/movies/{id}";

    @Test
    @DisplayName("GET /api/v1/auth/movies/{id} must return status 200 when find by id successfully")
    void findById_MustReturnStatus200_WhenFindByIdSuccessfully() throws Exception {
      when(movieClientMock.findById("1000000")).thenReturn(MovieFaker.movie());
      when(movieMapperMock.toMovieDetailsResponse(any())).thenReturn(MovieFaker.movieDetailsResponse());

      mockMvc.perform(get(FIND_BY_ID_URI, "1000000").contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/auth/movies/{id} must return MovieDetailsResponse when find by id successfully")
    void findById_MustReturnMovieDetailsResponse_WhenFindByIdSuccessfully() throws Exception {
      when(movieClientMock.findById("1000000")).thenReturn(MovieFaker.movie());
      when(movieMapperMock.toMovieDetailsResponse(any())).thenReturn(MovieFaker.movieDetailsResponse());

      String actualContent = mockMvc.perform(get(FIND_BY_ID_URI, "1000000").contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();

      MovieDetailsResponse actualResponseBody = mapper
          .readValue(actualContent, MovieDetailsResponse.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.id()).isNotNull().isEqualTo("1000000");
      assertThat(actualResponseBody.title()).isNotNull().isEqualTo("Lorem");
      assertThat(actualResponseBody.overview()).isNotNull().isEqualTo("Lorem ipsum dolor sit amet");
      assertThat(actualResponseBody.originalLanguage()).isNotNull().isEqualTo("en");
      assertThat(actualResponseBody.releaseDate()).isNotNull().isEqualTo("2024-01-16");
    }

    @Test
    @DisplayName("GET /api/v1/auth/movies/{id} must return status 400 when path variable movieId is blank")
    void findById_MustReturnStatus400_WhenRequestVariableMovieIdIsBlank() throws Exception {
      mockMvc.perform(get(FIND_BY_ID_URI, "  ").contentType(CONTENT_TYPE))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/v1/auth/movies/{id} must return ProblemDetail when path variable movieId is blank")
    void findById_MustReturnProblemDetail_WhenRequestPathVariableMovieIdIsBlank() throws Exception {
      String actualContent = mockMvc.perform(get(FIND_BY_ID_URI, "  ").contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getStatus()).isEqualTo(400);
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Bad request");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("Some fields are invalids");
      assertThat(actualResponseBody.getProperties()).isNotNull();
      assertThat(actualResponseBody.getProperties().get("fields")).isNotNull().isEqualTo("movieId");
      assertThat(actualResponseBody.getProperties().get("messages")).isNotNull()
          .isEqualTo("movie id must not be blank");
    }

    @Test
    @DisplayName("GET /api/v1/auth/movies/{id} must return status 404 when not found Movie")
    void findById_MustReturnStatus400_WhenNotFoundMovie() throws Exception {
      when(movieClientMock.findById("1000000"))
          .thenThrow(new MovieNotFoundException("movie not found with id: 1000000"));

      mockMvc.perform(get(FIND_BY_ID_URI, "1000000").contentType(CONTENT_TYPE))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/auth/movies/{id} must return ProblemDetail when not found Movie")
    void findById_MustReturnProblemDetail_WhenNotFoundMovie() throws Exception {
      when(movieClientMock.findById("1000000"))
          .thenThrow(new MovieNotFoundException("movie not found with id: 1000000"));

      String actualContent = mockMvc.perform(get(FIND_BY_ID_URI, "1000000").contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getStatus()).isEqualTo(404);
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Movie not found");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("movie not found with id: 1000000");
    }

    @Test
    @DisplayName("GET /api/v1/auth/movies/{id} must return status 500 when api_key is invalid")
    void findById_MustReturnStatus400_WhenAPIKEYIsInvalid() throws Exception {
      when(movieClientMock.findById("1000000"))
          .thenThrow(new InvalidTMDbApiKeyException("invalid TMDb API Key"));

      mockMvc.perform(get(FIND_BY_ID_URI, "1000000").contentType(CONTENT_TYPE))
          .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GET /api/v1/auth/movies/{id} must return ProblemDetail when api_key is invalid")
    void findById_MustReturnProblemDetail_WhenAPIKEYIsInvalid() throws Exception {
      when(movieClientMock.findById("1000000"))
          .thenThrow(new InvalidTMDbApiKeyException("invalid TMDb API Key"));

      String actualContent = mockMvc.perform(get(FIND_BY_ID_URI, "1000000").contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getStatus()).isEqualTo(500);
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Internal server error");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("invalid TMDb API Key");
    }

  }

}
