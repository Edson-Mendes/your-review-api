package br.com.emendes.yourreviewapi.mapper.impl;

import br.com.emendes.yourreviewapi.dto.request.ReviewRegisterRequest;
import br.com.emendes.yourreviewapi.dto.request.ReviewUpdateRequest;
import br.com.emendes.yourreviewapi.dto.response.*;
import br.com.emendes.yourreviewapi.mapper.ReviewMapper;
import br.com.emendes.yourreviewapi.model.entity.Review;
import br.com.emendes.yourreviewapi.repository.projection.ReviewDetailsProjection;
import br.com.emendes.yourreviewapi.repository.projection.ReviewSummaryProjection;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Implementação de {@link ReviewMapper}.
 */
@Component
public class ReviewMapperImpl implements ReviewMapper {

  @Override
  public Review toReview(ReviewRegisterRequest reviewRegisterRequest) {
    Assert.notNull(reviewRegisterRequest, "reviewRegisterRequest must not be null");

    return Review.builder()
        .vote(reviewRegisterRequest.vote())
        .opinion(reviewRegisterRequest.opinion())
        .build();
  }

  @Override
  public ReviewResponse toReviewResponse(Review review) {
    Assert.notNull(review, "review must not be null");

    return ReviewResponse.builder()
        .id(review.getId())
        .movieId(review.getMovieVotes().getMovieId())
        .vote(review.getVote())
        .opinion(review.getOpinion())
        .userId(review.getUser().getId())
        .createdAt(review.getCreatedAt())
        .build();
  }

  @Override
  public ReviewDetailsResponse toReviewDetailsResponse(
      ReviewDetailsProjection reviewDetailsProjection,
      MovieSummaryResponse movie) {
    Assert.notNull(reviewDetailsProjection, "reviewDetailsProjection must not be null");
    Assert.notNull(movie, "movie must not be null");

    return ReviewDetailsResponse.builder()
        .id(reviewDetailsProjection.getId())
        .movie(movie)
        .vote(reviewDetailsProjection.getVote())
        .opinion(reviewDetailsProjection.getOpinion())
        .userId(reviewDetailsProjection.getUserId())
        .createdAt(reviewDetailsProjection.getCreatedAt())
        .build();
  }

  @Override
  public ReviewSummaryResponse toReviewSummaryResponse(ReviewSummaryProjection reviewSummaryProjection) {
    Assert.notNull(reviewSummaryProjection, "reviewSummaryProjection must not be null");

    UserSummaryResponse userSummaryResponse = UserSummaryResponse.builder()
        .id(reviewSummaryProjection.getUserId())
        .name(reviewSummaryProjection.getUserName())
        .email(reviewSummaryProjection.getUserEmail())
        .build();

    return ReviewSummaryResponse.builder()
        .id(reviewSummaryProjection.getId())
        .movieId(reviewSummaryProjection.getMovieVotesMovieId())
        .vote(reviewSummaryProjection.getVote())
        .opinion(reviewSummaryProjection.getOpinion())
        .user(userSummaryResponse)
        .build();
  }

  @Override
  public void merge(Review review, ReviewUpdateRequest reviewUpdateRequest) {
    Assert.notNull(review, "review must not be null");
    Assert.notNull(reviewUpdateRequest, "reviewUpdateRequest must not be null");

    review.setVote(reviewUpdateRequest.vote());
    review.setOpinion(reviewUpdateRequest.opinion());
  }

}
