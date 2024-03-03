package br.com.emendes.yourreviewapi.util.faker;

import br.com.emendes.yourreviewapi.dto.response.ReviewDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewSummaryResponse;
import br.com.emendes.yourreviewapi.model.entity.Review;
import br.com.emendes.yourreviewapi.repository.projection.ReviewDetailsProjection;
import br.com.emendes.yourreviewapi.repository.projection.ReviewSummaryProjection;
import br.com.emendes.yourreviewapi.util.projection.impl.ReviewDetailsProjectionImpl;
import br.com.emendes.yourreviewapi.util.projection.impl.ReviewSummaryProjectionImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Classe para manter objetos fake relacionados a {@link Review} para uso em testes automatizados.
 */
public class ReviewFaker {

  public static final String REVIEW_OPINION = "Lorem ipsum dolor sit amet";
  public static final LocalDateTime REVIEW_CREATED_AT = LocalDateTime.parse("2024-02-09T10:00:00");
  public static final int REVIEW_VOTE = 9;
  public static final long REVIEW_ID = 2_000_000L;
  public static final String REVIEW_OPINION_UPDATED = "Lorem ipsum dolor sit amet updated";
  public static final int REVIEW_VOTE_UPDATED = 8;

  private ReviewFaker() {
  }

  /**
   * Retorna {@link Review} com os campos vote e opinion.
   */
  public static Review nonRegisteredReview() {
    return Review.builder()
        .vote(REVIEW_VOTE)
        .opinion(REVIEW_OPINION)
        .build();
  }

  /**
   * Retorna {@link Review} com o campo vote.
   */
  public static Review nonRegisteredReviewWithoutOpinion() {
    return Review.builder()
        .vote(REVIEW_VOTE)
        .build();
  }

  /**
   * Retorna {@link Review} com todos os campos.
   */
  public static Review review() {
    return Review.builder()
        .id(REVIEW_ID)
        .vote(REVIEW_VOTE)
        .opinion(REVIEW_OPINION)
        .user(UserFaker.user())
        .movieVotes(MovieVotesFaker.movieVotes())
        .createdAt(REVIEW_CREATED_AT)
        .build();
  }

  /**
   * Retorna {@code Optional<Review>} contendo o objeto.
   */
  public static Optional<Review> reviewOptional() {
    return Optional.of(review());
  }

  public static Review reviewUpdated() {
    return Review.builder()
        .id(REVIEW_ID)
        .vote(REVIEW_VOTE_UPDATED)
        .opinion(REVIEW_OPINION_UPDATED)
        .user(UserFaker.user())
        .movieVotes(MovieVotesFaker.movieVotes())
        .createdAt(REVIEW_CREATED_AT)
        .build();
  }

  /**
   * Retorna {@link Review} sem o campo opinion.
   */
  public static Review reviewWithoutOpinion() {
    return Review.builder()
        .id(REVIEW_ID)
        .vote(REVIEW_VOTE)
        .user(UserFaker.user())
        .movieVotes(MovieVotesFaker.movieVotes())
        .createdAt(REVIEW_CREATED_AT)
        .build();
  }

  /**
   * Retorna {@link Review} atualizado sem o campo opinion.
   */
  public static Review reviewUpdatedWithoutOpinion() {
    return Review.builder()
        .id(REVIEW_ID)
        .vote(REVIEW_VOTE_UPDATED)
        .user(UserFaker.user())
        .movieVotes(MovieVotesFaker.movieVotes())
        .createdAt(REVIEW_CREATED_AT)
        .build();
  }

  /**
   * Retorna um objeto {@link ReviewResponse} com todos os campos.
   */
  public static ReviewResponse reviewResponse() {
    return ReviewResponse.builder()
        .id(REVIEW_ID)
        .vote(REVIEW_VOTE)
        .opinion(REVIEW_OPINION)
        .userId(UserFaker.USER_ID)
        .movieId(MovieFaker.MOVIE_ID)
        .createdAt(REVIEW_CREATED_AT)
        .build();
  }

  /**
   * Retorna um objeto {@link ReviewResponse} sem o campo opinion.
   */
  public static ReviewResponse reviewResponseWithoutOpinion() {
    return ReviewResponse.builder()
        .id(REVIEW_ID)
        .vote(REVIEW_VOTE)
        .userId(UserFaker.USER_ID)
        .movieId(MovieFaker.MOVIE_ID)
        .createdAt(REVIEW_CREATED_AT)
        .build();
  }

  /**
   * Retorna um objeto {@link ReviewResponse} com dados atualizados.
   */
  public static ReviewResponse reviewResponseUpdated() {
    return ReviewResponse.builder()
        .id(REVIEW_ID)
        .vote(REVIEW_VOTE_UPDATED)
        .opinion(REVIEW_OPINION_UPDATED)
        .userId(UserFaker.USER_ID)
        .movieId(MovieFaker.MOVIE_ID)
        .createdAt(REVIEW_CREATED_AT)
        .build();
  }

  /**
   * Retorna um objeto {@link ReviewResponse} com dados atualizados e sem opinion.
   */
  public static ReviewResponse reviewResponseUpdatedWithoutOpinion() {
    return ReviewResponse.builder()
        .id(REVIEW_ID)
        .vote(REVIEW_VOTE_UPDATED)
        .userId(UserFaker.USER_ID)
        .movieId(MovieFaker.MOVIE_ID)
        .createdAt(REVIEW_CREATED_AT)
        .build();
  }

  /**
   * Retorna um objeto {@link ReviewDetailsResponse} com todos os campos.
   */
  public static ReviewDetailsResponse reviewDetailsResponse() {
    return ReviewDetailsResponse.builder()
        .id(REVIEW_ID)
        .vote(REVIEW_VOTE)
        .opinion(REVIEW_OPINION)
        .userId(UserFaker.USER_ID)
        .movie(MovieFaker.movieSummaryResponse())
        .createdAt(REVIEW_CREATED_AT)
        .build();
  }

  /**
   * Retorna um objeto {@code Page<Review>}.
   */
  public static Page<ReviewSummaryProjection> reviewSummaryProjectionPage() {
    Pageable pageable = PageRequest.of(0, 20);

    return new PageImpl<>(List.of(reviewSummaryProjection()), pageable, 1);
  }

  /**
   * Retorna {@link ReviewSummaryProjection} com todos os campos.
   */
  public static ReviewSummaryProjection reviewSummaryProjection() {
    return ReviewSummaryProjectionImpl.builder()
        .id(REVIEW_ID)
        .vote(REVIEW_VOTE)
        .opinion(REVIEW_OPINION)
        .userId(UserFaker.USER_ID)
        .movieVotesMovieId(MovieFaker.MOVIE_ID)
        .build();
  }

  /**
   * Retorna {@code Optional<ReviewDetailsProjection>} contendo o objeto.
   */
  public static Optional<ReviewDetailsProjection> reviewDetailsProjectionOptional() {
    return Optional.of(reviewDetailsProjection());
  }

  /**
   * Retorna {@link ReviewDetailsProjection} com todos os campos.
   */
  public static ReviewDetailsProjection reviewDetailsProjection() {
    return ReviewDetailsProjectionImpl.builder()
        .id(REVIEW_ID)
        .vote(REVIEW_VOTE)
        .opinion(REVIEW_OPINION)
        .userId(UserFaker.USER_ID)
        .movieVotesMovieId(MovieFaker.MOVIE_ID)
        .createdAt(REVIEW_CREATED_AT)
        .build();
  }

  /**
   * Retorna um objeto {@link ReviewSummaryResponse}.
   */
  public static ReviewSummaryResponse reviewSummaryResponse() {
    return ReviewSummaryResponse.builder()
        .id(REVIEW_ID)
        .vote(REVIEW_VOTE)
        .opinion(REVIEW_OPINION)
        .user(UserFaker.userSummaryResponse())
        .movieId(MovieFaker.MOVIE_ID)
        .build();
  }

  /**
   * Retorna empty {@code Page<ReviewSummaryProjection>}.
   */
  public static Page<ReviewSummaryProjection> emptyPage() {
    return new PageImpl<>(List.of(), PageRequest.of(0, 20), 0);
  }

}
