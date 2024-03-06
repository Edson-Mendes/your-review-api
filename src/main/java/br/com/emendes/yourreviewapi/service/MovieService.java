package br.com.emendes.yourreviewapi.service;

import br.com.emendes.yourreviewapi.dto.response.MovieDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.MovieSummaryResponse;
import br.com.emendes.yourreviewapi.exception.MovieNotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

/**
 * Interface service com as abstrações para manipulação de Movies.
 */
@Validated
public interface MovieService {

  /**
   * Busca filmes pelo nome.
   *
   * @param name nome do filme a ser buscado.
   * @param page número da página a ser buscada, paginação zero-based (começa em 0)..
   * @return {@code Page<MovieSummaryResponse>} objeto paginado com os filmes encontrados.
   */
  Page<MovieSummaryResponse> findByName(
      @NotBlank(message = "{MovieService.findByName.name.NotBlank.message}") String name,
      @PositiveOrZero(message = "{MovieService.findByName.page.PositiveOrZero.message}") int page);

  /**
   * Busca informações detalhadas do Movie por id.
   *
   * @param movieId identificador do filme a ser buscado.
   * @return MovieDetailsResponse objeto contendo informações detalhadas do filme encontrado.
   * @throws MovieNotFoundException caso o filme não seja encontrado para o dado {@code movieId}.
   */
  MovieDetailsResponse findDetailedById(@NotBlank(message = "{MovieService.findById.movieId.NotBlank.message}") String movieId);

  /**
   * Busca informações resumidas do Movie por id.
   *
   * @param movieId identificador do filme a ser buscado.
   * @return MovieSummaryResponse objeto contendo informações resumidas do filme encontrado.
   * @throws MovieNotFoundException caso o filme não seja encontrado para o dado {@code movieId}.
   */
  MovieSummaryResponse findSummarizedById(@NotBlank(message = "{MovieService.findById.movieId.NotBlank.message}") String movieId);

  /**
   * Verifica se existe Movie para o dado {@code movieId}
   *
   * @param movieId identificador do filme a ser verificado.
   * @return {@code true} caso exista Movie com o dado {@code movieId}, false caso contrário.
   */
  boolean existsMovieById(@NotBlank(message = "{MovieService.findById.movieId.NotBlank.message}") String movieId);
}
