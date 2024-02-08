package br.com.emendes.yourreviewapi.unit.mapper.impl;

import br.com.emendes.yourreviewapi.dto.request.ReviewRegisterRequest;
import br.com.emendes.yourreviewapi.dto.response.ReviewDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewSummaryResponse;
import br.com.emendes.yourreviewapi.mapper.impl.ReviewMapperImpl;
import br.com.emendes.yourreviewapi.model.entity.Review;
import br.com.emendes.yourreviewapi.util.faker.MovieVotesFaker;
import br.com.emendes.yourreviewapi.util.faker.UserFaker;
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
        .movieId("1000000")
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
  @DisplayName("toReviewSummaryResponse must return ReviewSummaryResponse when map successfully")
  void toReviewSummaryResponse_MustReturnReviewSummaryResponse_WhenMapSuccessfully() {
    Review review = Review.builder()
        .id(1_000_000_000L)
        .vote(9)
        .opinion("Lorem ipsum dolor sit amet")
        .createdAt(LocalDateTime.parse("2024-02-08T10:00:00"))
        .user(UserFaker.user())
        .movieVotes(MovieVotesFaker.movieVotes())
        .build();

    ReviewSummaryResponse actualReviewSummaryResponse = reviewMapper.toReviewSummaryResponse(review);

    assertThat(actualReviewSummaryResponse).isNotNull();
    assertThat(actualReviewSummaryResponse.id()).isNotNull().isEqualTo(1_000_000_000L);
    assertThat(actualReviewSummaryResponse.vote()).isEqualTo(9);
    assertThat(actualReviewSummaryResponse.opinion()).isNotNull().isEqualTo("Lorem ipsum dolor sit amet");
    assertThat(actualReviewSummaryResponse.userId()).isNotNull().isEqualTo(100L);
    assertThat(actualReviewSummaryResponse.movieId()).isNotNull().isEqualTo("1000000");
  }

  @Test
  @DisplayName("toReviewSummaryResponse must throw IllegalArgumentException when review parameter is null")
  void toReviewSummaryResponse_MustThrowIllegalArgumentException_WhenReviewParameterIsNull() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> reviewMapper.toReviewSummaryResponse(null))
        .withMessage("review must not be null");
  }

  @Test
  @DisplayName("toReviewDetailsResponse must return ReviewDetailsResponse when map successfully")
  void toReviewDetailsResponse_MustReturnReviewDetailsResponse_WhenMapSuccessfully() {
    Review review = Review.builder()
        .id(1_000_000_000L)
        .vote(9)
        .opinion("Lorem ipsum dolor sit amet")
        .createdAt(LocalDateTime.parse("2024-02-08T10:00:00"))
        .user(UserFaker.user())
        .movieVotes(MovieVotesFaker.movieVotes())
        .build();

    ReviewDetailsResponse actualReviewDetailsResponse = reviewMapper.toReviewDetailsResponse(review);

    assertThat(actualReviewDetailsResponse).isNotNull();
    assertThat(actualReviewDetailsResponse.id()).isNotNull().isEqualTo(1_000_000_000L);
    assertThat(actualReviewDetailsResponse.vote()).isEqualTo(9);
    assertThat(actualReviewDetailsResponse.opinion()).isNotNull().isEqualTo("Lorem ipsum dolor sit amet");
    assertThat(actualReviewDetailsResponse.createdAt()).isNotNull().isEqualTo("2024-02-08T10:00:00");
    assertThat(actualReviewDetailsResponse.userId()).isNotNull().isEqualTo(100L);
    assertThat(actualReviewDetailsResponse.movieId()).isNotNull().isEqualTo("1000000");
  }

  @Test
  @DisplayName("toReviewDetailsResponse must throw IllegalArgumentException when review parameter is null")
  void toReviewDetailsResponse_MustThrowIllegalArgumentException_WhenReviewParameterIsNull() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> reviewMapper.toReviewDetailsResponse(null))
        .withMessage("review must not be null");
  }

}