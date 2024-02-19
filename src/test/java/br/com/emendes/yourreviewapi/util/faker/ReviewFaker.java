package br.com.emendes.yourreviewapi.util.faker;

import br.com.emendes.yourreviewapi.dto.response.ReviewDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewSummaryResponse;
import br.com.emendes.yourreviewapi.model.entity.Review;
import br.com.emendes.yourreviewapi.repository.projection.ReviewSummaryProjection;
import br.com.emendes.yourreviewapi.util.projection.impl.ReviewSummaryProjectionImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe para manter objetos fake relacionados a {@link Review} para uso em testes automatizados.
 */
public class ReviewFaker {

  private ReviewFaker() {
  }

  /**
   * Retorna {@link Review} com os campos vote e opinion.
   */
  public static Review nonRegisteredReview() {
    return Review.builder()
        .vote(9)
        .opinion("Lorem ipsum dolor sit amet")
        .build();
  }

  /**
   * Retorna {@link Review} com todos os campos.
   */
  public static Review review() {
    return Review.builder()
        .id(2_000_000L)
        .vote(9)
        .opinion("Lorem ipsum dolor sit amet")
        .user(UserFaker.user())
        .movieVotes(MovieVotesFaker.movieVotes())
        .createdAt(LocalDateTime.parse("2024-02-09T10:00:00"))
        .build();
  }

  /**
   * Retorna um objeto {@link ReviewDetailsResponse} com todos os campos.
   */
  public static ReviewDetailsResponse reviewDetailsResponse() {
    return ReviewDetailsResponse.builder()
        .id(2_000_000L)
        .vote(9)
        .opinion("Lorem ipsum dolor sit amet")
        .userId(100L)
        .movieId("1000000")
        .createdAt(LocalDateTime.parse("2024-02-09T10:00:00"))
        .build();
  }

  /**
   * Retorna um objeto {@code Page<Review>}.
   */
  public static Page<ReviewSummaryProjection> reviewSummaryProjectionPage() {
    Pageable pageable = PageRequest.of(0, 20);

    return new PageImpl<>(List.of(reviewSummaryProjection()), pageable, 1);
  }

  public static ReviewSummaryProjection reviewSummaryProjection() {
    return ReviewSummaryProjectionImpl.builder()
        .id(1_000_000_000L)
        .vote(9)
        .opinion("Lorem ipsum dolor sit amet")
        .userId(100L)
        .movieVotesMovieId("1000000")
        .build();
  }

  /**
   * Retorna um objeto {@link ReviewSummaryResponse}.
   */
  public static ReviewSummaryResponse reviewSummaryResponse() {
    return ReviewSummaryResponse.builder()
        .id(2_000_000L)
        .vote(9)
        .opinion("Lorem ipsum dolor sit amet")
        .userId(100L)
        .movieId("1000000")
        .build();
  }
}
