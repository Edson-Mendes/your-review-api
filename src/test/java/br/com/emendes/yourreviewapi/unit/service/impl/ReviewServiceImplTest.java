package br.com.emendes.yourreviewapi.unit.service.impl;

import br.com.emendes.yourreviewapi.dto.request.ReviewRegisterRequest;
import br.com.emendes.yourreviewapi.dto.request.ReviewUpdateRequest;
import br.com.emendes.yourreviewapi.dto.response.ReviewDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewSummaryResponse;
import br.com.emendes.yourreviewapi.exception.ReviewAlreadyExistsException;
import br.com.emendes.yourreviewapi.exception.ReviewNotFoundException;
import br.com.emendes.yourreviewapi.exception.UserIsNotAuthenticatedException;
import br.com.emendes.yourreviewapi.mapper.ReviewMapper;
import br.com.emendes.yourreviewapi.model.entity.Review;
import br.com.emendes.yourreviewapi.repository.ReviewRepository;
import br.com.emendes.yourreviewapi.service.MovieService;
import br.com.emendes.yourreviewapi.service.MovieVotesService;
import br.com.emendes.yourreviewapi.service.impl.ReviewServiceImpl;
import br.com.emendes.yourreviewapi.util.component.AuthenticatedUserComponent;
import br.com.emendes.yourreviewapi.util.faker.MovieFaker;
import br.com.emendes.yourreviewapi.util.faker.MovieVotesFaker;
import br.com.emendes.yourreviewapi.util.faker.ReviewFaker;
import br.com.emendes.yourreviewapi.util.faker.UserFaker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
  @Mock
  private MovieService movieServiceMock;

  @Nested
  @DisplayName("Register Method")
  class RegisterMethod {

    @Captor
    private ArgumentCaptor<Review> reviewArgumentCaptor;

    @Test
    @DisplayName("register must return ReviewResponse when MovieVotes already exists and register successfully")
    void register_MustReturnReviewDetailsResponse_WhenMovieVotesAlreadyExistsAndRegisterSuccessfully() {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.existsByUserIdAndMovieId(100L, "1234")).thenReturn(false);
      when(movieVotesServiceMock.findByMovieId("1234")).thenReturn(MovieVotesFaker.movieVotesOptional());
      when(reviewMapperMock.toReview(any())).thenReturn(ReviewFaker.nonRegisteredReview());
      when(reviewRepositoryMock.save(any())).thenReturn(ReviewFaker.review());
      when(reviewMapperMock.toReviewResponse(any(Review.class))).thenReturn(ReviewFaker.reviewResponse());

      ReviewRegisterRequest reviewRegisterRequest = ReviewRegisterRequest.builder()
          .vote(9)
          .opinion("Lorem ipsum dolor sit amet")
          .movieId("1234")
          .build();

      ReviewResponse actualReviewResponse = reviewService.register(reviewRegisterRequest);

      assertThat(actualReviewResponse).isNotNull();
      assertThat(actualReviewResponse.id()).isNotNull().isEqualTo(2_000_000L);
      assertThat(actualReviewResponse.vote()).isEqualTo(9);
      assertThat(actualReviewResponse.opinion()).isNotNull().isEqualTo("Lorem ipsum dolor sit amet");
      assertThat(actualReviewResponse.userId()).isNotNull().isEqualTo(100L);
      assertThat(actualReviewResponse.movieId()).isNotNull().isEqualTo("1234");
      assertThat(actualReviewResponse.createdAt()).isNotNull().isEqualTo("2024-02-09T10:00:00");
    }

    @Test
    @DisplayName("register must update MovieVotes when MovieVotes already exists and register successfully")
    void register_MustUpdateMovieVotes_WhenRegisterSuccessfully() {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.existsByUserIdAndMovieId(100L, "1234")).thenReturn(false);
      when(movieVotesServiceMock.findByMovieId("1234")).thenReturn(MovieVotesFaker.movieVotesOptional());
      when(reviewMapperMock.toReview(any())).thenReturn(ReviewFaker.nonRegisteredReview());
      when(reviewRepositoryMock.save(any())).thenReturn(ReviewFaker.review());
      when(reviewMapperMock.toReviewResponse(any(Review.class))).thenReturn(ReviewFaker.reviewResponse());

      ReviewRegisterRequest reviewRegisterRequest = ReviewRegisterRequest.builder()
          .vote(9)
          .opinion("Lorem ipsum dolor sit amet")
          .movieId("1234")
          .build();

      reviewService.register(reviewRegisterRequest);

      verify(reviewRepositoryMock).save(reviewArgumentCaptor.capture());
      Review actualReview = reviewArgumentCaptor.getValue();

      assertThat(actualReview).isNotNull();
      assertThat(actualReview.getMovieVotes()).isNotNull();
      assertThat(actualReview.getMovieVotes().getVoteTotal()).isEqualTo(132L);
      assertThat(actualReview.getMovieVotes().getVoteCount()).isEqualTo(41L);
    }

    @Test
    @DisplayName("register must return ReviewResponse when MovieVotes non exists and register successfully")
    void register_MustReturnReviewDetailsResponse_WhenMovieVotesNonExistsAndRegisterSuccessfully() {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.existsByUserIdAndMovieId(100L, "1234")).thenReturn(false);
      when(movieVotesServiceMock.findByMovieId("1234")).thenReturn(Optional.empty());
      when(movieVotesServiceMock.generateNonVotedMovieVotes("1234"))
          .thenReturn(MovieVotesFaker.nonRegisteredMovieVotes());
      when(reviewMapperMock.toReview(any())).thenReturn(ReviewFaker.nonRegisteredReview());
      when(reviewRepositoryMock.save(any())).thenReturn(ReviewFaker.review());
      when(reviewMapperMock.toReviewResponse(any(Review.class))).thenReturn(ReviewFaker.reviewResponse());

      ReviewRegisterRequest reviewRegisterRequest = ReviewRegisterRequest.builder()
          .vote(9)
          .opinion("Lorem ipsum dolor sit amet")
          .movieId("1234")
          .build();

      ReviewResponse actualReviewResponse = reviewService.register(reviewRegisterRequest);

      assertThat(actualReviewResponse).isNotNull();
      assertThat(actualReviewResponse.id()).isNotNull().isEqualTo(2_000_000L);
      assertThat(actualReviewResponse.vote()).isEqualTo(9);
      assertThat(actualReviewResponse.opinion()).isNotNull().isEqualTo("Lorem ipsum dolor sit amet");
      assertThat(actualReviewResponse.userId()).isNotNull().isEqualTo(100L);
      assertThat(actualReviewResponse.movieId()).isNotNull().isEqualTo("1234");
      assertThat(actualReviewResponse.createdAt()).isNotNull().isEqualTo("2024-02-09T10:00:00");
    }

    @Test
    @DisplayName("register must update MovieVotes when MovieVotes non exists and register successfully")
    void register_MustUpdateMovieVotes_WhenMovieVotesNonExistsAndRegisterSuccessfully() {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.existsByUserIdAndMovieId(100L, "1234")).thenReturn(false);
      when(movieVotesServiceMock.findByMovieId("1234")).thenReturn(Optional.empty());
      when(movieVotesServiceMock.generateNonVotedMovieVotes("1234"))
          .thenReturn(MovieVotesFaker.nonRegisteredMovieVotes());
      when(reviewMapperMock.toReview(any())).thenReturn(ReviewFaker.nonRegisteredReview());
      when(reviewRepositoryMock.save(any())).thenReturn(ReviewFaker.review());
      when(reviewMapperMock.toReviewResponse(any(Review.class))).thenReturn(ReviewFaker.reviewResponse());

      ReviewRegisterRequest reviewRegisterRequest = ReviewRegisterRequest.builder()
          .vote(9)
          .opinion("Lorem ipsum dolor sit amet")
          .movieId("1234")
          .build();

      reviewService.register(reviewRegisterRequest);

      verify(reviewRepositoryMock).save(reviewArgumentCaptor.capture());
      Review actualReview = reviewArgumentCaptor.getValue();

      assertThat(actualReview).isNotNull();
      assertThat(actualReview.getMovieVotes()).isNotNull();
      assertThat(actualReview.getMovieVotes().getVoteTotal()).isEqualTo(9L);
      assertThat(actualReview.getMovieVotes().getVoteCount()).isEqualTo(1L);
    }

    @Test
    @DisplayName("register must throw ReviewAlreadyExistsException when user already has a review for movie")
    void register_MustThrowReviewAlreadyException_WhenUserAlreadyHasAReviewForMovie() {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.existsByUserIdAndMovieId(100L, "1234")).thenReturn(true);

      ReviewRegisterRequest reviewRegisterRequest = ReviewRegisterRequest.builder()
          .vote(9)
          .opinion("Lorem ipsum dolor sit amet")
          .movieId("1234")
          .build();

      assertThatExceptionOfType(ReviewAlreadyExistsException.class)
          .isThrownBy(() -> reviewService.register(reviewRegisterRequest))
          .withMessage("User john.doe@email.com has already reviewed the movie with id 1234");
    }

    @Test
    @DisplayName("register must throw UserIsNotAuthenticatedException when user is not authenticated")
    void register_MustThrowUserIsNotAuthenticatedException_WhenUserIsNotAuthenticated() {
      when(authenticatedUserComponentMock.getCurrentUser())
          .thenThrow(new UserIsNotAuthenticatedException("User is not authenticate"));

      ReviewRegisterRequest reviewRegisterRequest = ReviewRegisterRequest.builder()
          .vote(9)
          .opinion("Lorem ipsum dolor sit amet")
          .movieId("1234")
          .build();

      assertThatExceptionOfType(UserIsNotAuthenticatedException.class)
          .isThrownBy(() -> reviewService.register(reviewRegisterRequest))
          .withMessage("User is not authenticate");
    }

  }

  @Nested
  @DisplayName("FetchByMovieId Method")
  class FetchByMovieIdMethod {

    @Test
    @DisplayName("fetchByMovieId must return Page<ReviewSummaryResponse> when fetch successfully")
    void fetchByMovieId_MustReturnPageReviewSummaryResponse_WhenFetchSuccessfully() {
      when(movieVotesServiceMock.findByMovieId("1234")).thenReturn(MovieVotesFaker.movieVotesOptional());
      when(reviewRepositoryMock.findProjectedByMovieVotesMovieId(any(), any()))
          .thenReturn(ReviewFaker.reviewSummaryProjectionPage());
      when(reviewMapperMock.toReviewSummaryResponse(any())).thenReturn(ReviewFaker.reviewSummaryResponse());

      Page<ReviewSummaryResponse> actualReviewSummaryResponsePage =
          reviewService.fetchByMovieId("1234", 0);

      assertThat(actualReviewSummaryResponsePage).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    @DisplayName("fetchByMovieId must return empty Page when there are no reviews")
    void fetchByMovieId_MustReturnEmptyPage_WhenThereAreNoReviews() {
      when(reviewRepositoryMock.findProjectedByMovieVotesMovieId(any(), any()))
          .thenReturn(Page.empty());

      Page<ReviewSummaryResponse> actualReviewSummaryResponsePage = reviewService.fetchByMovieId("1234", 0);

      assertThat(actualReviewSummaryResponsePage).isNotNull().isEmpty();
    }

  }

  @Nested
  @DisplayName("FindById Method")
  class FindByIdMethod {

    @Test
    @DisplayName("findById must return ReviewDetailsResponse when found successfully")
    void findById_MustReturnReviewDetailsResponse_WhenFoundSuccessfully() {
      when(reviewRepositoryMock.findProjectedById(any())).thenReturn(ReviewFaker.reviewDetailsProjectionOptional());
      when(reviewMapperMock.toReviewDetailsResponse(any(), any())).thenReturn(ReviewFaker.reviewDetailsResponse());
      when(movieServiceMock.findSummarizedById(any())).thenReturn(MovieFaker.movieSummaryResponse());

      ReviewDetailsResponse actualReviewDetailsResponse = reviewService.findById(2_000_000L);

      assertThat(actualReviewDetailsResponse).isNotNull();
      assertThat(actualReviewDetailsResponse.id()).isNotNull().isEqualTo(2_000_000L);
      assertThat(actualReviewDetailsResponse.vote()).isEqualTo(9);
      assertThat(actualReviewDetailsResponse.opinion()).isNotNull().isEqualTo("Lorem ipsum dolor sit amet");
      assertThat(actualReviewDetailsResponse.userId()).isNotNull().isEqualTo(100L);
      assertThat(actualReviewDetailsResponse.createdAt()).isNotNull().isEqualTo("2024-02-09T10:00:00");
      assertThat(actualReviewDetailsResponse.movie()).isNotNull();
      assertThat(actualReviewDetailsResponse.movie().id()).isNotNull().isEqualTo("1234");
      assertThat(actualReviewDetailsResponse.movie().title()).isNotNull().isEqualTo("XPTO");
      assertThat(actualReviewDetailsResponse.movie().releaseDate()).isNotNull().isEqualTo("2024-01-16");
      assertThat(actualReviewDetailsResponse.movie().posterPath()).isNotNull().isEqualTo("/1234");
    }

    @Test
    @DisplayName("findById must throw ReviewNotFoundException when found not found review by id")
    void findById_MustThrowReviewNotFoundException_WhenNotFoundReviewById() {
      when(reviewRepositoryMock.findProjectedById(any())).thenReturn(Optional.empty());

      assertThatExceptionOfType(ReviewNotFoundException.class)
          .isThrownBy(() -> reviewService.findById(2_000_000L))
          .withMessage("Review not found for id: 2000000");
    }

  }

  @Nested
  @DisplayName("UpdateById Method")
  class UpdateByIdMethod {

    @Captor
    private ArgumentCaptor<Review> reviewArgumentCaptor;

    @Test
    @DisplayName("updateById must return ReviewResponse when update successfully")
    void updateById_MustReturnReviewResponse_WhenUpdateSuccessfully() {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.findByIdAndUserId(any(), any())).thenReturn(ReviewFaker.reviewOptional());
      when(reviewRepositoryMock.save(any())).thenReturn(ReviewFaker.reviewUpdated());
      when(reviewMapperMock.toReviewResponse(any())).thenReturn(ReviewFaker.reviewResponseUpdated());

      ReviewUpdateRequest reviewUpdateRequest = ReviewUpdateRequest.builder()
          .vote(8)
          .opinion("Lorem ipsum dolor sit amet updated")
          .build();

      ReviewResponse actualReviewResponse = reviewService.updateById(2_000_000L, reviewUpdateRequest);

      assertThat(actualReviewResponse).isNotNull();
      assertThat(actualReviewResponse.id()).isNotNull().isEqualTo(2_000_000L);
      assertThat(actualReviewResponse.vote()).isEqualTo(8);
      assertThat(actualReviewResponse.opinion()).isNotNull().isEqualTo("Lorem ipsum dolor sit amet updated");
      assertThat(actualReviewResponse.userId()).isNotNull().isEqualTo(100L);
      assertThat(actualReviewResponse.movieId()).isNotNull().isEqualTo("1234");
      assertThat(actualReviewResponse.createdAt()).isNotNull().isEqualTo("2024-02-09T10:00:00");
    }

    @Test
    @DisplayName("updateById must update MovieVotes when update review successfully")
    void updateById_MustUpdateMovieVotes_WhenUpdateReviewSuccessfully() {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.findByIdAndUserId(any(), any())).thenReturn(ReviewFaker.reviewOptional());
      when(reviewRepositoryMock.save(any())).thenReturn(ReviewFaker.reviewUpdated());
      when(reviewMapperMock.toReviewResponse(any())).thenReturn(ReviewFaker.reviewResponseUpdated());

      ReviewUpdateRequest reviewUpdateRequest = ReviewUpdateRequest.builder()
          .vote(8)
          .opinion("Lorem ipsum dolor sit amet updated")
          .build();

      reviewService.updateById(2_000_000L, reviewUpdateRequest);
      verify(reviewMapperMock).merge(reviewArgumentCaptor.capture(), any());

      Review actualReview = reviewArgumentCaptor.getValue();

      assertThat(actualReview).isNotNull();
      assertThat(actualReview.getMovieVotes()).isNotNull();
      assertThat(actualReview.getMovieVotes().getVoteTotal()).isEqualTo(122L);
      assertThat(actualReview.getMovieVotes().getVoteCount()).isEqualTo(40L);
    }

    @Test
    @DisplayName("updateById must throw UserIsNotAuthenticatedException when user is not authenticated")
    void updateById_MustThrowUserIsNotAuthenticatedException_WhenUserIsNotAuthenticated() {
      when(authenticatedUserComponentMock.getCurrentUser())
          .thenThrow(new UserIsNotAuthenticatedException("User is not authenticate"));

      ReviewUpdateRequest reviewUpdateRequest = ReviewUpdateRequest.builder()
          .vote(8)
          .opinion("Lorem ipsum dolor sit amet updated")
          .build();

      assertThatExceptionOfType(UserIsNotAuthenticatedException.class)
          .isThrownBy(() -> reviewService.updateById(2_000_000L, reviewUpdateRequest))
          .withMessage("User is not authenticate");
    }

    @Test
    @DisplayName("updateById must throw ReviewNotFoundException when not found review for given reviewId")
    void updateById_MustThrowReviewNotFoundException_WhenNotFoundReviewForGivenId() {
      when(authenticatedUserComponentMock.getCurrentUser())
          .thenReturn(UserFaker.user());
      when(reviewRepositoryMock.findByIdAndUserId(any(), any())).thenReturn(Optional.empty());

      ReviewUpdateRequest reviewUpdateRequest = ReviewUpdateRequest.builder()
          .vote(8)
          .opinion("Lorem ipsum dolor sit amet updated")
          .build();

      assertThatExceptionOfType(ReviewNotFoundException.class)
          .isThrownBy(() -> reviewService.updateById(2_000_000L, reviewUpdateRequest))
          .withMessage("Review not found for id: 2000000");
    }

  }

  @Nested
  @DisplayName("DeleteById Method")
  class DeleteByIdMethod {

    @Captor
    private ArgumentCaptor<Review> reviewArgumentCaptor;

    @Test
    @DisplayName("deleteById must update MovieVotes when delete successfully")
    void deleteById_MustUpdateMovieVotes_WhenDeleteSuccessfully() {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(reviewRepositoryMock.findByIdAndUserId(any(), any())).thenReturn(ReviewFaker.reviewOptional());

      reviewService.deleteById(2_000_000L);

      verify(reviewRepositoryMock).delete(reviewArgumentCaptor.capture());

      Review actualReview = reviewArgumentCaptor.getValue();

      assertThat(actualReview).isNotNull();
      assertThat(actualReview.getMovieVotes()).isNotNull();
      assertThat(actualReview.getMovieVotes().getVoteTotal()).isEqualTo(114L);
      assertThat(actualReview.getMovieVotes().getVoteCount()).isEqualTo(39L);
    }

    @Test
    @DisplayName("deleteById must throw UserIsNotAuthenticatedException when user is not authenticated")
    void deleteById_MustThrowUserIsNotAuthenticatedException_WhenUserIsNotAuthenticated() {
      when(authenticatedUserComponentMock.getCurrentUser())
          .thenThrow(new UserIsNotAuthenticatedException("User is not authenticate"));

      assertThatExceptionOfType(UserIsNotAuthenticatedException.class)
          .isThrownBy(() -> reviewService.deleteById(2_000_000L))
          .withMessage("User is not authenticate");
    }

    @Test
    @DisplayName("deleteById must throw ReviewNotFoundException when not found review for given reviewId")
    void deleteById_MustThrowReviewNotFoundException_WhenNotFoundReviewForGivenId() {
      when(authenticatedUserComponentMock.getCurrentUser())
          .thenReturn(UserFaker.user());
      when(reviewRepositoryMock.findByIdAndUserId(any(), any())).thenReturn(Optional.empty());

      assertThatExceptionOfType(ReviewNotFoundException.class)
          .isThrownBy(() -> reviewService.deleteById(2_000_000L))
          .withMessage("Review not found for id: 2000000");
    }

  }

}
