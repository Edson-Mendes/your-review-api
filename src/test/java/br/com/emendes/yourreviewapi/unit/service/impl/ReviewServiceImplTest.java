package br.com.emendes.yourreviewapi.unit.service.impl;

import br.com.emendes.yourreviewapi.dto.request.ReviewRegisterRequest;
import br.com.emendes.yourreviewapi.dto.response.ReviewDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewSummaryResponse;
import br.com.emendes.yourreviewapi.exception.ReviewAlreadyExistsException;
import br.com.emendes.yourreviewapi.mapper.ReviewMapper;
import br.com.emendes.yourreviewapi.repository.ReviewRepository;
import br.com.emendes.yourreviewapi.service.MovieVotesService;
import br.com.emendes.yourreviewapi.service.impl.ReviewServiceImpl;
import br.com.emendes.yourreviewapi.util.component.AuthenticatedUserComponent;
import br.com.emendes.yourreviewapi.util.faker.MovieVotesFaker;
import br.com.emendes.yourreviewapi.util.faker.ReviewFaker;
import br.com.emendes.yourreviewapi.util.faker.UserFaker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests de ReviewServiceImpl")
class ReviewServiceImplTest {

  @InjectMocks
  private ReviewServiceImpl reviewService;
  @Mock
  private AuthenticatedUserComponent authenticatedUserComponentMock;
  @Mock
  private MovieVotesService movieVotesServiceMock;
  @Mock
  private ReviewMapper reviewMapperMock;
  @Mock
  private ReviewRepository reviewRepositoryMock;

  @Nested
  @DisplayName("Register Method")
  class RegisterMethod {

    @Test
    @DisplayName("register must return ReviewDetailsResponse when MovieVotes already exists and register successfully")
    void register_MustReturnReviewDetailsResponse_WhenMovieVotesAlreadyExistsAndRegisterSuccessfully() {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.existsByUserIdAndMovieId(100L, "1000000")).thenReturn(false);
      when(movieVotesServiceMock.findByMovieId("1000000")).thenReturn(MovieVotesFaker.movieVotesOptional());
      when(reviewMapperMock.toReview(any())).thenReturn(ReviewFaker.nonRegisteredReview());
      when(reviewRepositoryMock.save(any())).thenReturn(ReviewFaker.review());
      when(reviewMapperMock.toReviewDetailsResponse(any())).thenReturn(ReviewFaker.reviewDetailsResponse());

      ReviewRegisterRequest reviewRegisterRequest = ReviewRegisterRequest.builder()
          .vote(9)
          .opinion("Lorem ipsum dolor sit amet")
          .movieId("1000000")
          .build();

      ReviewDetailsResponse actualReviewDetailsResponse = reviewService.register(reviewRegisterRequest);

      assertThat(actualReviewDetailsResponse).isNotNull();
      assertThat(actualReviewDetailsResponse.id()).isNotNull().isEqualTo(2_000_000L);
      assertThat(actualReviewDetailsResponse.vote()).isEqualTo(9);
      assertThat(actualReviewDetailsResponse.opinion()).isNotNull().isEqualTo("Lorem ipsum dolor sit amet");
      assertThat(actualReviewDetailsResponse.userId()).isNotNull().isEqualTo(100L);
      assertThat(actualReviewDetailsResponse.movieId()).isNotNull().isEqualTo("1000000");
      assertThat(actualReviewDetailsResponse.createdAt()).isNotNull().isEqualTo("2024-02-09T10:00:00");
    }

    @Test
    @DisplayName("register must return ReviewDetailsResponse when MovieVotes non exists and register successfully")
    void register_MustReturnReviewDetailsResponse_WhenMovieVotesNonExistsAndRegisterSuccessfully() {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.existsByUserIdAndMovieId(100L, "1000000")).thenReturn(false);
      when(movieVotesServiceMock.findByMovieId("1000000")).thenReturn(Optional.empty());
      when(movieVotesServiceMock.generateNonVotedMovieVotes("1000000"))
          .thenReturn(MovieVotesFaker.nonRegisteredMovieVotes());
      when(reviewMapperMock.toReview(any())).thenReturn(ReviewFaker.nonRegisteredReview());
      when(reviewRepositoryMock.save(any())).thenReturn(ReviewFaker.review());
      when(reviewMapperMock.toReviewDetailsResponse(any())).thenReturn(ReviewFaker.reviewDetailsResponse());

      ReviewRegisterRequest reviewRegisterRequest = ReviewRegisterRequest.builder()
          .vote(9)
          .opinion("Lorem ipsum dolor sit amet")
          .movieId("1000000")
          .build();

      ReviewDetailsResponse actualReviewDetailsResponse = reviewService.register(reviewRegisterRequest);

      assertThat(actualReviewDetailsResponse).isNotNull();
      assertThat(actualReviewDetailsResponse.id()).isNotNull().isEqualTo(2_000_000L);
      assertThat(actualReviewDetailsResponse.vote()).isEqualTo(9);
      assertThat(actualReviewDetailsResponse.opinion()).isNotNull().isEqualTo("Lorem ipsum dolor sit amet");
      assertThat(actualReviewDetailsResponse.userId()).isNotNull().isEqualTo(100L);
      assertThat(actualReviewDetailsResponse.movieId()).isNotNull().isEqualTo("1000000");
      assertThat(actualReviewDetailsResponse.createdAt()).isNotNull().isEqualTo("2024-02-09T10:00:00");
    }

    @Test
    @DisplayName("register must throw ReviewAlreadyExistsException when  user already has a review for movie")
    void register_MustThrowReviewAlreadyException_WhenUserAlreadyHasAReviewForMovie() {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.existsByUserIdAndMovieId(100L, "1000000")).thenReturn(true);

      ReviewRegisterRequest reviewRegisterRequest = ReviewRegisterRequest.builder()
          .vote(9)
          .opinion("Lorem ipsum dolor sit amet")
          .movieId("1000000")
          .build();

      assertThatExceptionOfType(ReviewAlreadyExistsException.class)
          .isThrownBy(() -> reviewService.register(reviewRegisterRequest))
          .withMessage("User lorem@email.com has already reviewed the movie with id 1000000");
    }

  }

  @Nested
  @DisplayName("FetchByMovieId Method")
  class fetchByMovieIdMethod {

    @Test
    @DisplayName("fetchByMovieId must return Page<ReviewSummaryResponse> when fetch successfully")
    void fetchByMovieId_MustReturnPageReviewSummaryResponse_WhenFetchSuccessfully() {
      when(movieVotesServiceMock.findByMovieId("1000000")).thenReturn(MovieVotesFaker.movieVotesOptional());
      when(reviewRepositoryMock.findByMovieVotes(any(), any())).thenReturn(ReviewFaker.reviewPage());
      when(reviewMapperMock.toReviewSummaryResponse(any())).thenReturn(ReviewFaker.reviewSummaryResponse());

      Page<ReviewSummaryResponse> actualReviewSummaryResponsePage = reviewService.fetchByMovieId("1000000", 0);

      assertThat(actualReviewSummaryResponsePage).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    @DisplayName("fetchByMovieId must return empty Page when there are no reviews")
    void fetchByMovieId_MustReturnEmptyPage_WhenThereAreNoReviews() {
      when(movieVotesServiceMock.findByMovieId("1000000")).thenReturn(Optional.empty());

      Page<ReviewSummaryResponse> actualReviewSummaryResponsePage = reviewService.fetchByMovieId("1000000", 0);

      assertThat(actualReviewSummaryResponsePage).isNotNull().isEmpty();
    }

  }

}
