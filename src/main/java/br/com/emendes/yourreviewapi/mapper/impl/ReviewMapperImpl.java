package br.com.emendes.yourreviewapi.mapper.impl;

import br.com.emendes.yourreviewapi.dto.request.ReviewRegisterRequest;
import br.com.emendes.yourreviewapi.dto.response.ReviewDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewSummaryResponse;
import br.com.emendes.yourreviewapi.mapper.ReviewMapper;
import br.com.emendes.yourreviewapi.model.entity.Review;
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
  public ReviewDetailsResponse toReviewDetailsResponse(Review review) {
    Assert.notNull(review, "review must not be null");

    return ReviewDetailsResponse.builder()
        .id(review.getId())
        .movieId(review.getMovieVotes().getMovieId())
        .vote(review.getVote())
        .opinion(review.getOpinion())
        .userId(review.getUser().getId())
        .build();
  }

  @Override
  public ReviewSummaryResponse toReviewSummaryResponse(Review review) {
    Assert.notNull(review, "review must not be null");

    return ReviewSummaryResponse.builder()
        .id(review.getId())
        .movieId(review.getMovieVotes().getMovieId())
        .vote(review.getVote())
        .opinion(review.getOpinion())
        .userId(review.getUser().getId())
        .build();
  }

}
