package br.com.emendes.yourreviewapi.client;

import br.com.emendes.yourreviewapi.exception.MovieNotFoundException;
import br.com.emendes.yourreviewapi.model.Movie;
import org.springframework.data.domain.Page;

/**
 * Interface client com as abstrações para interação com API de movies.
 */
public interface MovieClient {

  /**
   * Busca Movies por nome.
   *
   * @param name nome do filme a ser buscado.
   * @param page página a ser buscada.
   */
  Page<Movie> findByName(String name, int page);

  /**
   * Busca Movie por id através de uma API de filmes.
   *
   * @param movieId identificador do filme.
   * @return Movie objeto que representa um filme.
   * @throws MovieNotFoundException caso o filme não seja encontrado.
   */
  Movie findById(String movieId);

}
