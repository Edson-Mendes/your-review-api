package br.com.emendes.yourreviewapi.integration.controller;

import br.com.emendes.yourreviewapi.controller.ReviewController;
import br.com.emendes.yourreviewapi.dto.response.ReviewDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewSummaryResponse;
import br.com.emendes.yourreviewapi.exception.UserIsNotAuthenticatedException;
import br.com.emendes.yourreviewapi.mapper.ReviewMapper;
import br.com.emendes.yourreviewapi.model.entity.Review;
import br.com.emendes.yourreviewapi.repository.ReviewRepository;
import br.com.emendes.yourreviewapi.service.MovieService;
import br.com.emendes.yourreviewapi.service.MovieVotesService;
import br.com.emendes.yourreviewapi.service.ReviewService;
import br.com.emendes.yourreviewapi.util.PageableResponse;
import br.com.emendes.yourreviewapi.util.component.AuthenticatedUserComponent;
import br.com.emendes.yourreviewapi.util.faker.MovieFaker;
import br.com.emendes.yourreviewapi.util.faker.MovieVotesFaker;
import br.com.emendes.yourreviewapi.util.faker.ReviewFaker;
import br.com.emendes.yourreviewapi.util.faker.UserFaker;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    excludeAutoConfiguration = {RedisAutoConfiguration.class, SecurityAutoConfiguration.class},
    useDefaultFilters = false,
    includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ReviewController.class, ReviewService.class}),
        @ComponentScan.Filter(classes = {RestControllerAdvice.class})
    }
)
@DisplayName("Test dos endpoints de ReviewController")
class ReviewControllerIT {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper mapper;
  @MockBean
  private AuthenticatedUserComponent authenticatedUserComponentMock;
  @MockBean
  private MovieVotesService movieVotesServiceMock;
  @MockBean
  private MovieService movieServiceMock;
  @MockBean
  private ReviewMapper reviewMapperMock;
  @MockBean
  private ReviewRepository reviewRepositoryMock;

  private final String CONTENT_TYPE = "application/json;charset=UTF-8";

  @Nested
  @DisplayName("Tests for register endpoint")
  class RegisterEndpoint {

    private static final String REGISTER_URI = "/api/v1/reviews";

    @Test
    @DisplayName("POST /api/v1/reviews must return status 201 when register review successfully")
    void register_MustReturnStatus201_WhenRegisterReviewSuccessfully() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.existsByUserIdAndMovieId(any(), any())).thenReturn(false);
      when(movieVotesServiceMock.findByMovieId("1234")).thenReturn(MovieVotesFaker.movieVotesOptional());
      when(reviewMapperMock.toReview(any())).thenReturn(ReviewFaker.nonRegisteredReview());
      when(reviewRepositoryMock.save(any())).thenReturn(ReviewFaker.review());
      when(reviewMapperMock.toReviewResponse(any(Review.class))).thenReturn(ReviewFaker.reviewResponse());

      String requestBody = """
          {
            "vote": 9,
            "opinion": "Lorem ipsum dolor sit amet",
            "movieId": "1234"
          }
          """;

      mockMvc.perform(post(REGISTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /api/v1/reviews must return ReviewDetailsResponse when register review successfully")
    void register_MustReturnReviewDetailsResponse_WhenRegisterReviewSuccessfully() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.existsByUserIdAndMovieId(any(), any())).thenReturn(false);
      when(movieVotesServiceMock.findByMovieId("1234")).thenReturn(MovieVotesFaker.movieVotesOptional());
      when(reviewMapperMock.toReview(any())).thenReturn(ReviewFaker.nonRegisteredReview());
      when(reviewRepositoryMock.save(any())).thenReturn(ReviewFaker.review());
      when(reviewMapperMock.toReviewResponse(any(Review.class))).thenReturn(ReviewFaker.reviewResponse());

      String requestBody = """
          {
            "vote": 9,
            "opinion": "Lorem ipsum dolor sit amet",
            "movieId": "1234"
          }
          """;

      String actualContent = mockMvc.perform(post(REGISTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();

      ReviewResponse actualResponseBody = mapper.readValue(actualContent, ReviewResponse.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.id()).isNotNull().isEqualTo(2_000_000L);
      assertThat(actualResponseBody.vote()).isEqualTo(9);
      assertThat(actualResponseBody.opinion()).isNotNull().isEqualTo("Lorem ipsum dolor sit amet");
      assertThat(actualResponseBody.userId()).isNotNull().isEqualTo(100L);
      assertThat(actualResponseBody.movieId()).isNotNull().isEqualTo("1234");
      assertThat(actualResponseBody.createdAt()).isNotNull();
    }

    @Test
    @DisplayName("POST /api/v1/reviews must return status 201 when no one reviewed movie with given movieId")
    void register_MustReturnStatus201_WhenNoOneReviewedMovieWithGivenMovieId() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.existsByUserIdAndMovieId(any(), any())).thenReturn(false);
      when(movieVotesServiceMock.findByMovieId("1234")).thenReturn(Optional.empty());
      when(movieVotesServiceMock.generateNonVotedMovieVotes("1234"))
          .thenReturn(MovieVotesFaker.nonRegisteredMovieVotes());
      when(reviewMapperMock.toReview(any())).thenReturn(ReviewFaker.nonRegisteredReview());
      when(reviewRepositoryMock.save(any())).thenReturn(ReviewFaker.review());
      when(reviewMapperMock.toReviewResponse(any(Review.class))).thenReturn(ReviewFaker.reviewResponse());

      String requestBody = """
          {
            "vote": 9,
            "opinion": "Lorem ipsum dolor sit amet",
            "movieId": "1234"
          }
          """;

      mockMvc.perform(post(REGISTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /api/v1/reviews must return ReviewDetailsResponse when no one reviewed movie with given movieId")
    void register_MustReturnReviewDetailsResponse_WhenNoOneReviewedMovieWithGivenMovieId() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.existsByUserIdAndMovieId(any(), any())).thenReturn(false);
      when(movieVotesServiceMock.findByMovieId("1234")).thenReturn(Optional.empty());
      when(movieVotesServiceMock.generateNonVotedMovieVotes("1234"))
          .thenReturn(MovieVotesFaker.nonRegisteredMovieVotes());
      when(reviewMapperMock.toReview(any())).thenReturn(ReviewFaker.nonRegisteredReview());
      when(reviewRepositoryMock.save(any())).thenReturn(ReviewFaker.review());
      when(reviewMapperMock.toReviewResponse(any(Review.class))).thenReturn(ReviewFaker.reviewResponse());

      String requestBody = """
          {
            "vote": 9,
            "opinion": "Lorem ipsum dolor sit amet",
            "movieId": "1234"
          }
          """;

      String actualContent = mockMvc.perform(post(REGISTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();

      ReviewResponse actualResponseBody = mapper.readValue(actualContent, ReviewResponse.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.id()).isNotNull().isEqualTo(2_000_000L);
      assertThat(actualResponseBody.vote()).isEqualTo(9);
      assertThat(actualResponseBody.opinion()).isNotNull().isEqualTo("Lorem ipsum dolor sit amet");
      assertThat(actualResponseBody.userId()).isNotNull().isEqualTo(100L);
      assertThat(actualResponseBody.movieId()).isNotNull().isEqualTo("1234");
    }

    @Test
    @DisplayName("POST /api/v1/reviews must return status 201 when register review without opinion successfully")
    void register_MustReturnStatus201_WhenRegisterReviewWithoutOpinionSuccessfully() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.existsByUserIdAndMovieId(any(), any())).thenReturn(false);
      when(movieVotesServiceMock.findByMovieId("1234")).thenReturn(MovieVotesFaker.movieVotesOptional());
      when(reviewMapperMock.toReview(any())).thenReturn(ReviewFaker.nonRegisteredReviewWithoutOpinion());
      when(reviewRepositoryMock.save(any())).thenReturn(ReviewFaker.reviewWithoutOpinion());
      when(reviewMapperMock.toReviewResponse(any(Review.class)))
          .thenReturn(ReviewFaker.reviewResponseWithoutOpinion());

      String requestBody = """
          {
            "vote": 9,
            "movieId": "1234"
          }
          """;

      mockMvc.perform(post(REGISTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /api/v1/reviews must return ReviewDetailsResponse when register review without opinion successfully")
    void register_MustReturnReviewDetailsResponse_WhenRegisterReviewWithoutOpinionSuccessfully() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.existsByUserIdAndMovieId(any(), any())).thenReturn(false);
      when(movieVotesServiceMock.findByMovieId("1234")).thenReturn(MovieVotesFaker.movieVotesOptional());
      when(reviewMapperMock.toReview(any())).thenReturn(ReviewFaker.nonRegisteredReviewWithoutOpinion());
      when(reviewRepositoryMock.save(any())).thenReturn(ReviewFaker.reviewWithoutOpinion());
      when(reviewMapperMock.toReviewResponse(any(Review.class)))
          .thenReturn(ReviewFaker.reviewResponseWithoutOpinion());

      String requestBody = """
          {
            "vote": 9,
            "movieId": "1234"
          }
          """;

      String actualContent = mockMvc.perform(post(REGISTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();

      ReviewResponse actualResponseBody = mapper.readValue(actualContent, ReviewResponse.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.id()).isNotNull().isEqualTo(2_000_000L);
      assertThat(actualResponseBody.vote()).isEqualTo(9);
      assertThat(actualResponseBody.opinion()).isNull();
      assertThat(actualResponseBody.userId()).isNotNull().isEqualTo(100L);
      assertThat(actualResponseBody.movieId()).isNotNull().isEqualTo("1234");
    }

    @Test
    @DisplayName("POST /api/v1/reviews must return status 400 when request body has invalid fields")
    void register_MustReturnStatus201_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
          {
            "vote": 12,
            "opinion": "",
            "movieId": "1234"
          }
          """;

      mockMvc.perform(post(REGISTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/reviews must return ProblemDetail when request body has invalid fields")
    void register_MustReturnProblemDetail_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
          {
            "vote": 12,
            "opinion": "",
            "movieId": "1234"
          }
          """;

      String actualContent = mockMvc.perform(post(REGISTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Bad request");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("Some fields are invalids");
      assertThat(actualResponseBody.getStatus()).isEqualTo(400);
      assertThat(actualResponseBody.getProperties()).isNotNull();
      assertThat(actualResponseBody.getProperties().get("fields")).isNotNull();
      assertThat(actualResponseBody.getProperties().get("messages")).isNotNull();

      String[] actualFields = ((String) actualResponseBody.getProperties().get("fields")).split(";");
      String[] actualMessages = ((String) actualResponseBody.getProperties().get("messages")).split(";");

      assertThat(actualFields).isNotNull().hasSize(2).contains("vote", "opinion");
      assertThat(actualMessages).isNotNull().hasSize(2)
          .contains("vote must be less than or equal to 10", "opinion must not be empty or blank");
    }

    @Test
    @DisplayName("POST /api/v1/reviews must return status 500 when user is not authenticated")
    void register_MustReturnStatus500_WhenUserIsNotAuthenticated() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser())
          .thenThrow(new UserIsNotAuthenticatedException("User is not authenticate"));

      String requestBody = """
          {
            "vote": 9,
            "opinion": "Lorem ipsum dolor sit amet",
            "movieId": "1234"
          }
          """;

      mockMvc.perform(post(REGISTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("POST /api/v1/reviews must return ProblemDetail when user is not authenticated")
    void register_MustReturnProblemDetail_WhenUserIsNotAuthenticated() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser())
          .thenThrow(new UserIsNotAuthenticatedException("User is not authenticate"));

      String requestBody = """
          {
            "vote": 9,
            "opinion": "Lorem ipsum dolor sit amet",
            "movieId": "1234"
          }
          """;

      String actualContent = mockMvc.perform(post(REGISTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Internal server error");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("User is not authenticate");
      assertThat(actualResponseBody.getStatus()).isEqualTo(500);
    }

    @Test
    @DisplayName("POST /api/v1/reviews must return status 400 when user already register review for given movieId")
    void register_MustReturnStatus400_WhenUserAlreadyRegisterReviewForGivenMovieId() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.existsByUserIdAndMovieId(any(), any())).thenReturn(true);

      String requestBody = """
          {
            "vote": 9,
            "opinion": "Lorem ipsum dolor sit amet",
            "movieId": "1234"
          }
          """;

      mockMvc.perform(post(REGISTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/reviews must return ProblemDetail when user already register review for given movieId")
    void register_MustReturnProblemDetail_WhenUserAlreadyRegisterReviewForGivenMovieId() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.existsByUserIdAndMovieId(any(), any())).thenReturn(true);

      String requestBody = """
          {
            "vote": 9,
            "opinion": "Lorem ipsum dolor sit amet",
            "movieId": "1234"
          }
          """;

      String actualContent = mockMvc.perform(post(REGISTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Bad request");
      assertThat(actualResponseBody.getDetail()).isNotNull()
          .isEqualTo("User john.doe@email.com has already reviewed the movie with id 1234");
      assertThat(actualResponseBody.getStatus()).isEqualTo(400);
    }

  }

  @Nested
  @DisplayName("Tests for fetchByMovieId endpoint")
  class FetchByMovieIdEndpoint {

    private static final String FETCH_BY_MOVIE_ID_URI = "/api/v1/reviews";

    @Test
    @DisplayName("GET /api/v1/reviews?movieId={movieId} must return status 200 when fetch successfully")
    void fetchByMovieId_MustReturnStatus200_WhenFetchSuccessfully() throws Exception {
      when(reviewRepositoryMock.findProjectedByMovieVotesMovieId(any(), any()))
          .thenReturn(ReviewFaker.reviewSummaryProjectionPage());
      when(reviewMapperMock.toReviewSummaryResponse(any())).thenReturn(ReviewFaker.reviewSummaryResponse());

      mockMvc.perform(get(FETCH_BY_MOVIE_ID_URI).param("movieId", "1234").contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/reviews?movieId={movieId} must return Page<ReviewSummaryResponse> when fetch successfully")
    void fetchByMovieId_MustReturnPageReviewSummaryResponse_WhenFetchSuccessfully() throws Exception {
      when(reviewRepositoryMock.findProjectedByMovieVotesMovieId(any(), any()))
          .thenReturn(ReviewFaker.reviewSummaryProjectionPage());
      when(reviewMapperMock.toReviewSummaryResponse(any())).thenReturn(ReviewFaker.reviewSummaryResponse());

      String actualContent = mockMvc.perform(get(FETCH_BY_MOVIE_ID_URI).param("movieId", "1234").contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();

      Page<ReviewSummaryResponse> actualResponseBody = mapper.readValue(actualContent,
          new TypeReference<PageableResponse<ReviewSummaryResponse>>() {
          });

      assertThat(actualResponseBody).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    @DisplayName("GET /api/v1/reviews?movieId={movieId} must return status 200 when not found review for given movieId")
    void fetchByMovieId_MustReturnStatus200_WhenNotFoundReviewForGivenMovieId() throws Exception {
      when(reviewRepositoryMock.findProjectedByMovieVotesMovieId(any(), any()))
          .thenReturn(ReviewFaker.emptyPage());
      when(reviewMapperMock.toReviewSummaryResponse(any())).thenReturn(ReviewFaker.reviewSummaryResponse());

      mockMvc.perform(get(FETCH_BY_MOVIE_ID_URI).param("movieId", "1234").contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/reviews?movieId={movieId} must return empty Page<ReviewSummaryResponse> when not found review for given movieId")
    void fetchByMovieId_MustReturnEmptyPageReviewSummaryResponse_WhenNotFoundReviewForGivenMovieId() throws Exception {
      when(reviewRepositoryMock.findProjectedByMovieVotesMovieId(any(), any()))
          .thenReturn(ReviewFaker.emptyPage());
      when(reviewMapperMock.toReviewSummaryResponse(any())).thenReturn(ReviewFaker.reviewSummaryResponse());

      String actualContent = mockMvc.perform(get(FETCH_BY_MOVIE_ID_URI).param("movieId", "1234").contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();

      Page<ReviewSummaryResponse> actualResponseBody = mapper.readValue(actualContent,
          new TypeReference<PageableResponse<ReviewSummaryResponse>>() {
          });

      assertThat(actualResponseBody).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("GET /api/v1/reviews?movieId={movieId} must return status 400 when request param movieId is blank")
    void fetchByMovieId_MustReturnStatus400_WhenRequestParamMovieIdIsBlank() throws Exception {
      mockMvc.perform(get(FETCH_BY_MOVIE_ID_URI).param("movieId", "  ").contentType(CONTENT_TYPE))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/v1/reviews?movieId={movieId} must return ProblemDetail when request param movieId is blank")
    void fetchByMovieId_MustReturnProblemDetail_WhenRequestParamMovieIdIsBlank() throws Exception {
      String actualContent = mockMvc.perform(get(FETCH_BY_MOVIE_ID_URI).param("movieId", "  ").contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Bad request");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("Some fields are invalids");
      assertThat(actualResponseBody.getProperties()).isNotNull();
      assertThat(actualResponseBody.getProperties().get("fields")).isNotNull().isEqualTo("movieId");
      assertThat(actualResponseBody.getProperties().get("messages")).isNotNull()
          .isEqualTo("movie id must not be blank");
    }

    @Test
    @DisplayName("GET /api/v1/reviews?movieId={movieId} must return status 400 when request param page is negative")
    void fetchByMovieId_MustReturnStatus400_WhenRequestParamPageIsNegative() throws Exception {
      mockMvc.perform(get(FETCH_BY_MOVIE_ID_URI)
              .param("movieId", "1234")
              .param("page", "-1")
              .contentType(CONTENT_TYPE))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/v1/reviews?movieId={movieId} must return ProblemDetail when request param page is negative")
    void fetchByMovieId_MustReturnProblemDetail_WhenRequestParamPageIsNegative() throws Exception {
      String actualContent = mockMvc.perform(get(FETCH_BY_MOVIE_ID_URI)
              .param("movieId", "1234")
              .param("page", "-1")
              .contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Bad request");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("Some fields are invalids");
      assertThat(actualResponseBody.getProperties()).isNotNull();
      assertThat(actualResponseBody.getProperties().get("fields")).isNotNull().isEqualTo("page");
      assertThat(actualResponseBody.getProperties().get("messages")).isNotNull()
          .isEqualTo("page must be positive or zero");
    }

  }

  @Nested
  @DisplayName("Tests for findById endpoint")
  class FindByIdEndpoint {

    private static final String FETCH_BY_MOVIE_ID_URI = "/api/v1/reviews/{reviewId}";

    @Test
    @DisplayName("GET /api/v1/reviews/{movieId} must return status 200 when found successfully")
    void findById_MustReturnStatus200_WhenFetchSuccessfully() throws Exception {
      when(reviewRepositoryMock.findProjectedById(any()))
          .thenReturn(ReviewFaker.reviewDetailsProjectionOptional());
      when(movieServiceMock.findSummarizedById("1234")).thenReturn(MovieFaker.movieSummaryResponse());
      when(reviewMapperMock.toReviewDetailsResponse(any(), any()))
          .thenReturn(ReviewFaker.reviewDetailsResponse());

      mockMvc.perform(get(FETCH_BY_MOVIE_ID_URI, "1000000000").contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/reviews/{movieId} must return ReviewDetailsResponse when found successfully")
    void findById_MustReturnReviewDetailsResponse_WhenFetchSuccessfully() throws Exception {
      when(reviewRepositoryMock.findProjectedById(any()))
          .thenReturn(ReviewFaker.reviewDetailsProjectionOptional());
      when(movieServiceMock.findSummarizedById("1234")).thenReturn(MovieFaker.movieSummaryResponse());
      when(reviewMapperMock.toReviewDetailsResponse(any(), any()))
          .thenReturn(ReviewFaker.reviewDetailsResponse());

      String actualContent = mockMvc.perform(get(FETCH_BY_MOVIE_ID_URI, "2000000").contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();

      ReviewDetailsResponse actualResponseBody = mapper.readValue(actualContent, ReviewDetailsResponse.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.id()).isNotNull().isEqualTo(2_000_000L);
      assertThat(actualResponseBody.vote()).isEqualTo(9);
      assertThat(actualResponseBody.opinion()).isNotNull().isEqualTo("Lorem ipsum dolor sit amet");
      assertThat(actualResponseBody.userId()).isNotNull().isEqualTo(100L);
      assertThat(actualResponseBody.movie()).isNotNull();
      assertThat(actualResponseBody.movie().id()).isNotNull().isEqualTo("1234");
      assertThat(actualResponseBody.movie().title()).isNotNull().isEqualTo("XPTO");
      assertThat(actualResponseBody.movie().posterPath()).isNotNull().isEqualTo("/1234");
      assertThat(actualResponseBody.movie().releaseDate()).isNotNull().isEqualTo("2024-01-16");
    }

    @Test
    @DisplayName("GET /api/v1/reviews/{movieId} must return status 404 when not found review for given id")
    void findById_MustReturnStatus404_WhenNotFoundReviewForGivenId() throws Exception {
      when(reviewRepositoryMock.findProjectedById(any())).thenReturn(Optional.empty());

      mockMvc.perform(get(FETCH_BY_MOVIE_ID_URI, "2000000").contentType(CONTENT_TYPE))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/reviews/{movieId} must return ProblemDetail when not found review for given id")
    void findById_MustReturnProblemDetail_WhenNotFoundReviewForGivenId() throws Exception {
      when(reviewRepositoryMock.findProjectedById(any())).thenReturn(Optional.empty());

      String actualContent = mockMvc.perform(get(FETCH_BY_MOVIE_ID_URI, "2000000").contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Review not found");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("Review not found for id: 2000000");
      assertThat(actualResponseBody.getStatus()).isEqualTo(404);
    }

  }

  @Nested
  @DisplayName("Tests for updateById endpoint")
  class UpdateByIdEndpoint {

    private static final String UPDATE_BY_ID_URI = "/api/v1/reviews/{reviewId}";

    @Test
    @DisplayName("PUT /api/v1/reviews/{reviewId} must return status 200 when update review successfully")
    void updateById_MustReturnStatus200_WhenUpdateReviewSuccessfully() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.findByIdAndUserId(any(), any())).thenReturn(ReviewFaker.reviewOptional());
      when(reviewRepositoryMock.save(any())).thenReturn(ReviewFaker.reviewUpdated());
      when(reviewMapperMock.toReviewResponse(any(Review.class)))
          .thenReturn(ReviewFaker.reviewResponseUpdated());

      String requestBody = """
          {
            "vote": 8,
            "opinion": "Lorem ipsum dolor sit amet updated"
          }
          """;

      mockMvc.perform(put(UPDATE_BY_ID_URI, "2000000").contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/v1/reviews/{reviewId} must return ReviewDetailsResponse when update review successfully")
    void updateById_MustReturnReviewDetailsResponse_WhenUpdateReviewSuccessfully() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.findByIdAndUserId(any(), any())).thenReturn(ReviewFaker.reviewOptional());
      when(reviewRepositoryMock.save(any())).thenReturn(ReviewFaker.reviewUpdated());
      when(reviewMapperMock.toReviewResponse(any(Review.class)))
          .thenReturn(ReviewFaker.reviewResponseUpdated());

      String requestBody = """
          {
            "vote": 8,
            "opinion": "Lorem ipsum dolor sit amet updated"
          }
          """;

      String actualContent = mockMvc.perform(put(UPDATE_BY_ID_URI, "2000000").contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();

      ReviewResponse actualResponseBody = mapper.readValue(actualContent, ReviewResponse.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.id()).isNotNull().isEqualTo(2_000_000L);
      assertThat(actualResponseBody.vote()).isEqualTo(8);
      assertThat(actualResponseBody.opinion()).isNotNull().isEqualTo("Lorem ipsum dolor sit amet updated");
      assertThat(actualResponseBody.userId()).isNotNull().isEqualTo(100L);
      assertThat(actualResponseBody.movieId()).isNotNull().isEqualTo("1234");
      assertThat(actualResponseBody.createdAt()).isNotNull();
    }

    @Test
    @DisplayName("PUT /api/v1/reviews/{reviewId} must return status 200 when update review without opinion")
    void updateById_MustReturnStatus200_WhenUpdateReviewWithoutOpinion() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.findByIdAndUserId(any(), any())).thenReturn(ReviewFaker.reviewOptional());
      when(reviewRepositoryMock.save(any())).thenReturn(ReviewFaker.reviewUpdatedWithoutOpinion());
      when(reviewMapperMock.toReviewResponse(any(Review.class)))
          .thenReturn(ReviewFaker.reviewResponseUpdatedWithoutOpinion());

      String requestBody = """
          {
            "vote": 8
          }
          """;

      mockMvc.perform(put(UPDATE_BY_ID_URI, "2000000").contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/v1/reviews/{reviewId} must return ReviewDetailsResponse when update review successfully")
    void updateById_MustReturnReviewDetailsResponse_WhenUpdateReviewWithoutOpinion() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.findByIdAndUserId(any(), any())).thenReturn(ReviewFaker.reviewOptional());
      when(reviewRepositoryMock.save(any())).thenReturn(ReviewFaker.reviewUpdatedWithoutOpinion());
      when(reviewMapperMock.toReviewResponse(any(Review.class)))
          .thenReturn(ReviewFaker.reviewResponseUpdatedWithoutOpinion());

      String requestBody = """
          {
            "vote": 8
          }
          """;

      String actualContent = mockMvc.perform(put(UPDATE_BY_ID_URI, "2000000").contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();

      ReviewResponse actualResponseBody = mapper.readValue(actualContent, ReviewResponse.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.id()).isNotNull().isEqualTo(2_000_000L);
      assertThat(actualResponseBody.vote()).isEqualTo(8);
      assertThat(actualResponseBody.opinion()).isNull();
      assertThat(actualResponseBody.userId()).isNotNull().isEqualTo(100L);
      assertThat(actualResponseBody.movieId()).isNotNull().isEqualTo("1234");
      assertThat(actualResponseBody.createdAt()).isNotNull();
    }

    @Test
    @DisplayName("PUT /api/v1/reviews/{reviewId} must return status 400 when request body has invalid fields")
    void updateById_MustReturnStatus400_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
          {
            "vote": 12,
            "opinion": "  "
          }
          """;

      mockMvc.perform(put(UPDATE_BY_ID_URI, "2000000").contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/v1/reviews/{reviewId} must return ProblemDetail when request body has invalid fields")
    void updateById_MustReturnProblemDetail_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
          {
            "vote": 12,
            "opinion": "  "
          }
          """;

      String actualContent = mockMvc.perform(put(UPDATE_BY_ID_URI, "2000000").contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Bad request");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("Some fields are invalids");
      assertThat(actualResponseBody.getStatus()).isEqualTo(400);
      assertThat(actualResponseBody.getProperties()).isNotNull();
      assertThat(actualResponseBody.getProperties().get("fields")).isNotNull();
      assertThat(actualResponseBody.getProperties().get("messages")).isNotNull();

      String[] actualFields = ((String) actualResponseBody.getProperties().get("fields")).split(";");
      String[] actualMessages = ((String) actualResponseBody.getProperties().get("messages")).split(";");

      assertThat(actualFields).isNotNull().hasSize(2).contains("vote", "opinion");
      assertThat(actualMessages).isNotNull().hasSize(2)
          .contains("vote must be less than or equal to 10", "opinion must not be empty or blank");
    }

    @Test
    @DisplayName("PUT /api/v1/reviews/{reviewId} must return status 404 when not found review for given id")
    void updateById_MustReturnStatus404_WhenNotFoundReviewForGivenId() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.findByIdAndUserId(any(), any())).thenReturn(Optional.empty());

      String requestBody = """
          {
            "vote": 10,
            "opinion": "Lorem ipsum dolor sit amet updated"
          }
          """;

      mockMvc.perform(put(UPDATE_BY_ID_URI, "2000000").contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/v1/reviews/{reviewId} must return ProblemDetail when not found review for given id")
    void updateById_MustReturnProblemDetail_WhenNotFoundReviewForGivenId() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.findByIdAndUserId(any(), any())).thenReturn(Optional.empty());

      String requestBody = """
          {
            "vote": 10,
            "opinion": "Lorem ipsum dolor sit amet updated"
          }
          """;

      String actualContent = mockMvc.perform(put(UPDATE_BY_ID_URI, "2000000").contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Review not found");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("Review not found for id: 2000000");
      assertThat(actualResponseBody.getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("PUT /api/v1/reviews/{reviewId} must return status 500 when user is not authenticated")
    void updateById_MustReturnStatus500_WhenUserIsNotAuthenticated() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser())
          .thenThrow(new UserIsNotAuthenticatedException("User is not authenticate"));

      String requestBody = """
          {
            "vote": 8,
            "opinion": "Lorem ipsum dolor sit amet updated"
          }
          """;

      mockMvc.perform(put(UPDATE_BY_ID_URI, "2000000").contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("PUT /api/v1/reviews/{reviewId} must return ProblemDetail when user is not authenticated")
    void updateById_MustReturnProblemDetail_WhenUserIsNotAuthenticated() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser())
          .thenThrow(new UserIsNotAuthenticatedException("User is not authenticate"));

      String requestBody = """
          {
            "vote": 8,
            "opinion": "Lorem ipsum dolor sit amet updated"
          }
          """;

      String actualContent = mockMvc.perform(put(UPDATE_BY_ID_URI, "2000000")
              .contentType(CONTENT_TYPE)
              .content(requestBody))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Internal server error");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("User is not authenticate");
      assertThat(actualResponseBody.getStatus()).isEqualTo(500);
    }

  }

  @Nested
  @DisplayName("Tests for deleteById endpoint")
  class DeleteByIdEndpoint {

    private static final String DELETE_BY_ID_URI = "/api/v1/reviews/{reviewId}";

    @Captor
    private ArgumentCaptor<Review> reviewCaptor;

    @Test
    @DisplayName("DELETE /api/v1/reviews/{reviewId} must return status 204 when delete successfully")
    void deleteById_MustReturnStatus204_WhenDeleteSuccessfully() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.findByIdAndUserId(any(), any())).thenReturn(ReviewFaker.reviewOptional());

      mockMvc.perform(delete(DELETE_BY_ID_URI, "2000000").contentType(CONTENT_TYPE))
          .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/reviews/{reviewId} must update MovieVote.voteCount when delete successfully")
    void deleteById_MustUpdateMovieVotesVoteCount_WhenDeleteSuccessfully() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.findByIdAndUserId(any(), any())).thenReturn(ReviewFaker.reviewOptional());

      mockMvc.perform(delete(DELETE_BY_ID_URI, "2000000").contentType(CONTENT_TYPE))
          .andExpect(status().isNoContent());

      verify(reviewRepositoryMock).delete(reviewCaptor.capture());
      Review reviewCaptured = reviewCaptor.getValue();

      assertThat(reviewCaptured).isNotNull();
      assertThat(reviewCaptured.getMovieVotes()).isNotNull();
      assertThat(reviewCaptured.getMovieVotes().getVoteCount()).isEqualTo(39);
    }

    @Test
    @DisplayName("DELETE /api/v1/reviews/{reviewId} must update MovieVote.voteTotal when delete successfully")
    void deleteById_MustUpdateMovieVotesVoteTotal_WhenDeleteSuccessfully() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.findByIdAndUserId(any(), any())).thenReturn(ReviewFaker.reviewOptional());

      mockMvc.perform(delete(DELETE_BY_ID_URI, "2000000").contentType(CONTENT_TYPE))
          .andExpect(status().isNoContent());

      verify(reviewRepositoryMock).delete(reviewCaptor.capture());
      Review reviewCaptured = reviewCaptor.getValue();

      assertThat(reviewCaptured).isNotNull();
      assertThat(reviewCaptured.getMovieVotes()).isNotNull();
      assertThat(reviewCaptured.getMovieVotes().getVoteTotal()).isEqualTo(114);
    }

    @Test
    @DisplayName("DELETE /api/v1/reviews/{reviewId} must return status 404 when not found review for given id")
    void deleteById_MustReturnStatus404_WhenNotFoundReviewForGivenId() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.findByIdAndUserId(any(), any())).thenReturn(Optional.empty());

      mockMvc.perform(delete(DELETE_BY_ID_URI, "2000000").contentType(CONTENT_TYPE))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/v1/reviews/{reviewId} must return ProblemDetail when not found review for given id")
    void deleteById_MustReturnProblemDetail_WhenNotFoundReviewForGivenId() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.findByIdAndUserId(any(), any())).thenReturn(Optional.empty());

      String actualContent = mockMvc.perform(delete(DELETE_BY_ID_URI, "2000000").contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Review not found");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("Review not found for id: 2000000");
      assertThat(actualResponseBody.getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("DELETE /api/v1/reviews/{reviewId} must return status 500 when user is not authenticated")
    void deleteById_MustReturnStatus500_WhenUserIsNotAuthenticated() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser())
          .thenThrow(new UserIsNotAuthenticatedException("User is not authenticate"));

      mockMvc.perform(delete(DELETE_BY_ID_URI, "2000000").contentType(CONTENT_TYPE))
          .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("DELETE /api/v1/reviews/{reviewId} must return ProblemDetail when user is not authenticated")
    void deleteById_MustReturnProblemDetail_WhenUserIsNotAuthenticated() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser())
          .thenThrow(new UserIsNotAuthenticatedException("User is not authenticate"));

      String actualContent = mockMvc.perform(delete(DELETE_BY_ID_URI, "2000000").contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Internal server error");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("User is not authenticate");
      assertThat(actualResponseBody.getStatus()).isEqualTo(500);
    }

  }

}
