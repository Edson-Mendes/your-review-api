package br.com.emendes.yourreviewapi.unit.mapper.impl;

import br.com.emendes.yourreviewapi.dto.request.ReviewRegisterRequest;
import br.com.emendes.yourreviewapi.dto.request.ReviewUpdateRequest;
import br.com.emendes.yourreviewapi.dto.response.MovieSummaryResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewSummaryResponse;
import br.com.emendes.yourreviewapi.mapper.impl.ReviewMapperImpl;
import br.com.emendes.yourreviewapi.model.entity.Review;
import br.com.emendes.yourreviewapi.repository.projection.ReviewDetailsProjection;
import br.com.emendes.yourreviewapi.repository.projection.ReviewSummaryProjection;
import br.com.emendes.yourreviewapi.util.faker.MovieFaker;
import br.com.emendes.yourreviewapi.util.faker.MovieVotesFaker;
import br.com.emendes.yourreviewapi.util.faker.UserFaker;
import br.com.emendes.yourreviewapi.util.projection.impl.ReviewDetailsProjectionImpl;
import br.com.emendes.yourreviewapi.util.projection.impl.ReviewSummaryProjectionImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Unit tests de UserMapperImpl")
class ReviewMapperImplTest {

  private final ReviewMapperImpl reviewMapper = new ReviewMapperImpl();

  @Test
  @DisplayName("toReview must return Review when map successfully")
  void toReview_MustReturnReview_WhenMapSuccessfully() {
    ReviewRegisterRequest review = ReviewRegisterRequest.builder()
        .vote(9)
        .opinion("Lorem ipsum dolor sit amet")
        .movieId("1234")
        .build();

    Review actualReview = reviewMapper.toReview(review);

    assertThat(actualReview).isNotNull();
    assertThat(actualReview.getId()).isNull();
    assertThat(actualReview.getUser()).isNull();
    assertThat(actualReview.getMovieVotes()).isNull();
    assertThat(actualReview.getCreatedAt()).isNull();
    assertThat(actualReview.getVote()).isEqualTo(9);
    assertThat(actualReview.getOpinion()).isNotNull().isEqualTo("Lorem ipsum dolor sit amet");
  }

  @Test
  @DisplayName("toReview must throw IllegalArgumentException when reviewRegisterRequest parameter is null")
  void toReview_MustThrowIllegalArgumentException_WhenReviewRegisterRequestParameterIsNull() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> reviewMapper.toReview(null))
        .withMessage("reviewRegisterRequest must not be null");
  }

  @Test
  @DisplayName("toReviewSummaryResponse must return ReviewSummaryResponse when map ReviewSummaryProjection successfully")
  void toReviewSummaryResponse_MustReturnReviewSummaryResponse_WhenMapReviewSummaryProjectionSuccessfully() {
    ReviewSummaryProjection reviewSummaryProjection = ReviewSummaryProjectionImpl.builder()
        .id(2_000_000L)
        .vote(9)
        .opinion("Lorem ipsum dolor sit amet")
        .userId(100L)
        .userName("John Doe")
        .userEmail("john.doe@email.com")
        .movieVotesMovieId("1234")
        .build();

    ReviewSummaryResponse actualReviewSummaryResponse = reviewMapper.toReviewSummaryResponse(reviewSummaryProjection);

    assertThat(actualReviewSummaryResponse).isNotNull();
    assertThat(actualReviewSummaryResponse.id()).isNotNull().isEqualTo(2_000_000L);
    assertThat(actualReviewSummaryResponse.vote()).isEqualTo(9);
    assertThat(actualReviewSummaryResponse.opinion()).isNotNull().isEqualTo("Lorem ipsum dolor sit amet");
    assertThat(actualReviewSummaryResponse.user()).isNotNull();
    assertThat(actualReviewSummaryResponse.user().id()).isNotNull().isEqualTo(100L);
    assertThat(actualReviewSummaryResponse.user().name()).isNotNull().isEqualTo("John Doe");
    assertThat(actualReviewSummaryResponse.user().email()).isNotNull().isEqualTo("john.doe@email.com");
    assertThat(actualReviewSummaryResponse.movieId()).isNotNull().isEqualTo("1234");
  }

  @Test
  @DisplayName("toReviewSummaryResponse must throw IllegalArgumentException when review parameter is null")
  void toReviewSummaryResponse_MustThrowIllegalArgumentException_WhenReviewParameterIsNull() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> reviewMapper.toReviewSummaryResponse(null))
        .withMessage("reviewSummaryProjection must not be null");
  }

  @Test
  @DisplayName("toReviewResponse must return ReviewResponse when map Review successfully")
  void toReviewResponse_MustReturnReviewResponse_WhenMapSuccessfully() {
    Review review = Review.builder()
        .id(2_000_000L)
        .vote(9)
        .opinion("Lorem ipsum dolor sit amet")
        .createdAt(LocalDateTime.parse("2024-02-08T10:00:00"))
        .user(UserFaker.user())
        .movieVotes(MovieVotesFaker.movieVotes())
        .build();

    ReviewResponse actualReviewResponse = reviewMapper.toReviewResponse(review);

    assertThat(actualReviewResponse).isNotNull();
    assertThat(actualReviewResponse.id()).isNotNull().isEqualTo(2_000_000L);
    assertThat(actualReviewResponse.vote()).isEqualTo(9);
    assertThat(actualReviewResponse.opinion()).isNotNull().isEqualTo("Lorem ipsum dolor sit amet");
    assertThat(actualReviewResponse.createdAt()).isNotNull().isEqualTo("2024-02-08T10:00:00");
    assertThat(actualReviewResponse.userId()).isNotNull().isEqualTo(100L);
    assertThat(actualReviewResponse.movieId()).isNotNull().isEqualTo("1234");
  }

  @Test
  @DisplayName("toReviewResponse must throw IllegalArgumentException when review parameter is null")
  void toReviewResponse_MustThrowIllegalArgumentException_WhenReviewParameterIsNull() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> reviewMapper.toReviewResponse(null))
        .withMessage("review must not be null");
  }

  @Test
  @DisplayName("toReviewDetailsResponse must return ReviewDetailsResponse when map ReviewDetailsProjection successfully")
  void toReviewDetailsResponse_MustReturnReviewDetailsResponse_WhenMapReviewDetailsProjectionSuccessfully() {
    ReviewDetailsProjection reviewDetailsProjection = ReviewDetailsProjectionImpl.builder()
        .id(2_000_000L)
        .vote(9)
        .opinion("Lorem ipsum dolor sit amet")
        .createdAt(LocalDateTime.parse("2024-02-08T10:00:00"))
        .userId(100L)
        .movieVotesMovieId("1234")
        .build();

    ReviewDetailsResponse actualReviewDetailsResponse = reviewMapper
        .toReviewDetailsResponse(reviewDetailsProjection, MovieFaker.movieSummaryResponse());

    assertThat(actualReviewDetailsResponse).isNotNull();
    assertThat(actualReviewDetailsResponse.id()).isNotNull().isEqualTo(2_000_000L);
    assertThat(actualReviewDetailsResponse.vote()).isEqualTo(9);
    assertThat(actualReviewDetailsResponse.opinion()).isNotNull().isEqualTo("Lorem ipsum dolor sit amet");
    assertThat(actualReviewDetailsResponse.createdAt()).isNotNull().isEqualTo("2024-02-08T10:00:00");
    assertThat(actualReviewDetailsResponse.userId()).isNotNull().isEqualTo(100L);
    assertThat(actualReviewDetailsResponse.movie()).isNotNull();
    assertThat(actualReviewDetailsResponse.movie().id()).isNotNull().isEqualTo("1234");
    assertThat(actualReviewDetailsResponse.movie().title()).isNotNull().isEqualTo("XPTO");
    assertThat(actualReviewDetailsResponse.movie().posterPath()).isNotNull().isEqualTo("/1234");
    assertThat(actualReviewDetailsResponse.movie().releaseDate()).isNotNull().isEqualTo("2024-01-16");
  }

  @Test
  @DisplayName("toReviewDetailsResponse must throw IllegalArgumentException when reviewDetailsProjection parameter is null")
  void toReviewDetailsResponse_MustThrowIllegalArgumentException_WhenReviewDetailsProjectionParameterIsNull() {
    MovieSummaryResponse movieSummaryResponse = MovieFaker.movieSummaryResponse();

    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> reviewMapper.toReviewDetailsResponse(null, movieSummaryResponse))
        .withMessage("reviewDetailsProjection must not be null");
  }

  @Test
  @DisplayName("toReviewDetailsResponse must throw IllegalArgumentException when movie parameter is null")
  void toReviewDetailsResponse_MustThrowIllegalArgumentException_WhenMovieParameterIsNull() {
    ReviewDetailsProjection reviewDetailsProjection = ReviewDetailsProjectionImpl.builder()
        .id(2_000_000L)
        .vote(9)
        .opinion("Lorem ipsum dolor sit amet")
        .createdAt(LocalDateTime.parse("2024-02-08T10:00:00"))
        .userId(100L)
        .movieVotesMovieId("1234")
        .build();

    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> reviewMapper.toReviewDetailsResponse(reviewDetailsProjection, null))
        .withMessage("movie must not be null");
  }

  @Test
  @DisplayName("merge must update review according to reviewUpdateRequest when merge successfully")
  void merge_MustUpdateReviewAccordingToReviewUpdateRequest_WhenMergeSuccessfully() {
    Review review = Review.builder()
        .id(2_000_000L)
        .vote(9)
        .opinion("Lorem ipsum dolor sit amet")
        .createdAt(LocalDateTime.parse("2024-02-08T10:00:00"))
        .user(UserFaker.user())
        .movieVotes(MovieVotesFaker.movieVotes())
        .build();

    ReviewUpdateRequest reviewUpdateRequest = ReviewUpdateRequest.builder()
        .vote(8)
        .opinion("Lorem ipsum dolor sit amet updated")
        .build();

    reviewMapper.merge(review, reviewUpdateRequest);

    assertThat(review).isNotNull();
    assertThat(review.getId()).isNotNull().isEqualTo(2_000_000L);
    assertThat(review.getVote()).isEqualTo(8);
    assertThat(review.getOpinion()).isNotNull().isEqualTo("Lorem ipsum dolor sit amet updated");
    assertThat(review.getCreatedAt()).isNotNull().isEqualTo("2024-02-08T10:00:00");
    assertThat(review.getUser()).isNotNull().isEqualTo(UserFaker.user());
    assertThat(review.getMovieVotes()).isNotNull().isEqualTo(MovieVotesFaker.movieVotes());
  }

  @Test
  @DisplayName("merge must update review opinion to null when reviewUpdateRequest opinion is null")
  void merge_MustUpdateReviewOpinionToNull_WhenReviewUpdateRequestOpinionIsNull() {
    Review review = Review.builder()
        .id(2_000_000L)
        .vote(9)
        .opinion("Lorem ipsum dolor sit amet")
        .createdAt(LocalDateTime.parse("2024-02-08T10:00:00"))
        .user(UserFaker.user())
        .movieVotes(MovieVotesFaker.movieVotes())
        .build();

    ReviewUpdateRequest reviewUpdateRequest = ReviewUpdateRequest.builder()
        .vote(8)
        .opinion(null)
        .build();

    reviewMapper.merge(review, reviewUpdateRequest);

    assertThat(review).isNotNull();
    assertThat(review.getId()).isNotNull().isEqualTo(2_000_000L);
    assertThat(review.getVote()).isEqualTo(8);
    assertThat(review.getOpinion()).isNull();
    assertThat(review.getCreatedAt()).isNotNull().isEqualTo("2024-02-08T10:00:00");
    assertThat(review.getUser()).isNotNull().isEqualTo(UserFaker.user());
    assertThat(review.getMovieVotes()).isNotNull().isEqualTo(MovieVotesFaker.movieVotes());
  }

  @Test
  @DisplayName("merge must throw IllegalArgumentException when review is null")
  void merge_MustThrowIllegalArgumentException_WhenReviewIsNull() {
    ReviewUpdateRequest reviewUpdateRequest = ReviewUpdateRequest.builder()
        .vote(8)
        .opinion("Lorem ipsum dolor sit amet updated")
        .build();

    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> reviewMapper.merge(null, reviewUpdateRequest))
        .withMessage("review must not be null");
  }

  @Test
  @DisplayName("merge must throw IllegalArgumentException when reviewUpdateRequest is null")
  void merge_MustThrowIllegalArgumentException_WhenReviewUpdateRequestIsNull() {
    Review review = Review.builder()
        .id(2_000_000L)
        .vote(9)
        .opinion("Lorem ipsum dolor sit amet")
        .createdAt(LocalDateTime.parse("2024-02-08T10:00:00"))
        .user(UserFaker.user())
        .movieVotes(MovieVotesFaker.movieVotes())
        .build();

    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> reviewMapper.merge(review, null))
        .withMessage("reviewUpdateRequest must not be null");
  }

}