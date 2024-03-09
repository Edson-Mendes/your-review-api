package br.com.emendes.yourreviewapi.mapper;

import br.com.emendes.yourreviewapi.dto.MovieVotesAverage;
import br.com.emendes.yourreviewapi.dto.response.MovieDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.MovieSummaryResponse;
import br.com.emendes.yourreviewapi.dto.response.TMDbMovieResponse;
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
   * @param movie             objeto a ser mapeado para MovieDetailsResponse.
   * @param movieVotesAverage objeto contendo as informações de pontuação do Movie.
   * @return objeto MovieDetailsResponse com os dados de Movie.
   * @throws IllegalArgumentException caso {@code movie} seja null.
   */
  MovieDetailsResponse toMovieDetailsResponse(Movie movie, MovieVotesAverage movieVotesAverage);

  /**
   * Mapeia um objeto TMDbMovieResponse para Movie.
   *
   * @param tmdbMovieResponse objeto a ser mapeado para Movie.
   * @return objeto Movie com os dados de tmdbMovieResponse.
   * @throws IllegalArgumentException caso {@code tmdbMovieResponse} seja null.
   */
  Movie toMovie(TMDbMovieResponse tmdbMovieResponse);

}
