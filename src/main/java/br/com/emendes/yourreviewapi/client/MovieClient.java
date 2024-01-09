package br.com.emendes.yourreviewapi.client;

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

}
