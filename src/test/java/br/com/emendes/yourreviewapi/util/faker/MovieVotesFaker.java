package br.com.emendes.yourreviewapi.util.faker;

import br.com.emendes.yourreviewapi.model.entity.MovieVotes;

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
        .voteCount(123)
        .voteTotal(40)
        .movieId("1000000")
        .build();
  }

}
