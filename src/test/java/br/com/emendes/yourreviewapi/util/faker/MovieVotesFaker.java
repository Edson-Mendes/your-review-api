package br.com.emendes.yourreviewapi.util.faker;

import br.com.emendes.yourreviewapi.model.entity.MovieVotes;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Classe para manter objetos fake relacionados a {@link MovieVotes} para uso em testes automatizados.
 */
public class MovieVotesFaker {

  private MovieVotesFaker() {
  }

  /**
   * Retorna {@link MovieVotes} com todos os campos.
   */
  public static MovieVotes movieVotes() {
    return MovieVotes.builder()
        .id(1_000L)
        .voteCount(40)
        .voteTotal(123)
        .movieId("1000000")
        .createdAt(LocalDateTime.parse("2024-02-09T12:00:00"))
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
        .movieId("1000000")
        .createdAt(LocalDateTime.parse("2024-02-09T12:00:00"))
        .build();
  }
}
