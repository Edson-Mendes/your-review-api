package br.com.emendes.yourreviewapi.util.faker;

import br.com.emendes.yourreviewapi.dto.MovieVotesAverage;
import br.com.emendes.yourreviewapi.model.entity.MovieVotes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Classe para manter objetos fake relacionados a {@link MovieVotes} para uso em testes automatizados.
 */
public class MovieVotesFaker {

  public static final long MOVIE_VOTES_ID = 4_321L;
  public static final LocalDateTime MOVIE_VOTES_CREATED_AT = LocalDateTime.parse("2024-02-09T12:00:00");
  public static final long MOVIE_VOTES_REVIEW_TOTAL = 40;
  public static final BigDecimal REVIEW_AVERAGE = new BigDecimal("3.07");

  private MovieVotesFaker() {
  }

  /**
   * Retorna {@link MovieVotes} com todos os campos.
   */
  public static MovieVotes movieVotes() {
    return MovieVotes.builder()
        .id(MOVIE_VOTES_ID)
        .voteCount(MOVIE_VOTES_REVIEW_TOTAL)
        .voteTotal(123)
        .movieId(MovieFaker.MOVIE_ID)
        .createdAt(MOVIE_VOTES_CREATED_AT)
        .build();
  }

  /**
   * Retorna {@code Optional<MovieVotes>} com todos os campos.
   */
  public static Optional<MovieVotes> movieVotesOptional() {
    return Optional.of(movieVotes());
  }

  /**
   * Retorna {@link MovieVotes} com os campos
   */
  public static MovieVotes nonRegisteredMovieVotes() {
    return MovieVotes.builder()
        .voteCount(0)
        .voteTotal(0)
        .movieId(MovieFaker.MOVIE_ID)
        .createdAt(MOVIE_VOTES_CREATED_AT)
        .build();
  }

  /**
   * Retorna um objeto MovieVotesAverage com todos os campos.
   */
  public static MovieVotesAverage movieVotesAverage() {
    return MovieVotesAverage.builder()
        .id(MOVIE_VOTES_ID)
        .reviewTotal(MOVIE_VOTES_REVIEW_TOTAL)
        .reviewAverage(REVIEW_AVERAGE)
        .movieId("1234")
        .build();
  }

  /**
   * Retorna Optional<MovieVotesAverage> com todos os campos.
   */
  public static Optional<MovieVotesAverage> movieVotesAverageOptional() {
    return Optional.of(movieVotesAverage());
  }

}
