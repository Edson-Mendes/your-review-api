package br.com.emendes.yourreviewapi.mapper;

import br.com.emendes.yourreviewapi.dto.response.MovieDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.MovieSummaryResponse;
import br.com.emendes.yourreviewapi.model.Movie;

/**
 * Interface com as abstrações para mapeamento do recurso Movie.
 */
public interface MovieMapper {

  /**
   * Mapeia um objeto Movie para MovieSummaryResponse.
   *
   * @param movie objeto a ser mapeado para MovieSummaryResponse.
   * @return objeto MovieSummaryResponse com os dados de Movie.
   * @throws IllegalArgumentException caso {@code movie} seja null.
   */
  MovieSummaryResponse toMovieSummaryResponse(Movie movie);

  /**
   * Mapeia um objeto Movie para MovieDetailsResponse.
   *
   * @param movie objeto a ser mapeado para MovieDetailsResponse.
   * @return objeto MovieDetailsResponse com os dados de Movie.
   * @throws IllegalArgumentException caso {@code movie} seja null.
   */
  MovieDetailsResponse toMovieDetailsResponse(Movie movie);

}
