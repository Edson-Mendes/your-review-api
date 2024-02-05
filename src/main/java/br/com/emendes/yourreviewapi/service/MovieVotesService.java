package br.com.emendes.yourreviewapi.service;

import br.com.emendes.yourreviewapi.exception.MovieNotFoundException;
import br.com.emendes.yourreviewapi.exception.MovieVotesNotFoundException;
import br.com.emendes.yourreviewapi.model.Movie;
import br.com.emendes.yourreviewapi.model.entity.MovieVotes;

import java.util.Optional;

/**
 * Interface com as abstrações para manipulação do recurso MovieVotes.
 */
public interface MovieVotesService {

  /**
   * Busca MovieVotes por movieId.
   *
   * @param movieId identificador do Movie ao qual o MovieVotes está associado.
   * @return MovieVotes associado ao dado movieId.
   * @throws IllegalArgumentException caso movieId seja null, empty ou blank.
   */
  Optional<MovieVotes> findByMovieId(String movieId);

  /**
   * Atualiza voteTotal e voteCount da entidade MovieVotes.
   *
   * @param movieVotesId identificador do Movie ao qual MovieVotes está associado.
   * @param vote         novo valor do voto que deve ser juntado ao voteTotal e voteCount.
   * @throws IllegalArgumentException    caso vote seja menor que 1 ou maior que 10.
   * @throws MovieVotesNotFoundException caso MovieVotes não seja encontrado para o dado movieVotesId.
   */
  void updateById(Long movieVotesId, int vote);

  /**
   * Cadastrar {@link MovieVotes} para o filme com o dado movieId.
   *
   * @param movieId identificador do {@link Movie} que estará relacionado ao {@link MovieVotes}.
   * @return {@link MovieVotes} para o dado movieId.
   * @throws MovieNotFoundException caso movieId não corresponda a nenhum filme.
   */
  MovieVotes register(String movieId);

}
