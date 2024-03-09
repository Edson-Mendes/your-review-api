package br.com.emendes.yourreviewapi.service;

import br.com.emendes.yourreviewapi.dto.MovieVotesAverage;
import br.com.emendes.yourreviewapi.exception.MovieNotFoundException;
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
   * Gera um objeto {@link MovieVotes} com {@code voteCount} e {@code voteTotal} iguais a zero.
   *
   * @param movieId identificador do {@link Movie} relacionado ao {@link MovieVotes}.
   * @return objeto MovieVotes relacionado com o movieId e {@code voteCount} e {@code voteTotal} iguais a zero.
   * @throws MovieNotFoundException caso não exista um Movie para o dado {@code movieId}.
   */
  MovieVotes generateNonVotedMovieVotes(String movieId);

  /**
   * Busca {@link MovieVotesAverage} por movieId.
   *
   * @param movieId identificador do Movie ao qual o MovieVotes está associado.
   * @return {@code Optinal<MovieVotesAverage>} contendo MovieVotesAverage caso seja encontrado,
   * ou {@link Optional#empty()} caso contrário.
   */
  Optional<MovieVotesAverage> findAverageByMovieId(String movieId);

}
