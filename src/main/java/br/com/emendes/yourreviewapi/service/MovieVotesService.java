package br.com.emendes.yourreviewapi.service;

import br.com.emendes.yourreviewapi.exception.MovieVotesNotFoundException;
import br.com.emendes.yourreviewapi.model.entity.MovieVotes;

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
  MovieVotes findByMovieId(String movieId);

  /**
   * Atualiza voteTotal e voteCount da entidade MovieVotes.
   *
   * @param movieVotesId identificador do Movie ao qual MovieVotes está associado.
   * @param vote         novo valor do voto que deve ser juntado ao voteTotal e voteCount.
   * @throws IllegalArgumentException    caso vote seja menor que 1 ou maior que 10.
   * @throws MovieVotesNotFoundException caso MovieVotes não seja encontrado para o dado movieVotesId.
   */
  void updateById(Long movieVotesId, int vote);

}
