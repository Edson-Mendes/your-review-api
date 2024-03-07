package br.com.emendes.yourreviewapi.mapper.impl;

import br.com.emendes.yourreviewapi.dto.response.MovieDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.MovieSummaryResponse;
import br.com.emendes.yourreviewapi.dto.response.TMDbMovieResponse;
import br.com.emendes.yourreviewapi.mapper.MovieMapper;
import br.com.emendes.yourreviewapi.model.Movie;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Implementação de {@link MovieMapper}.
 */
@Component
public class MovieMapperImpl implements MovieMapper {

  @Override
  public MovieSummaryResponse toMovieSummaryResponse(Movie movie) {
    Assert.notNull(movie, "movie must not be null");

    return MovieSummaryResponse.builder()
        .id(movie.id())
        .title(movie.title())
        .releaseDate(movie.releaseDate())
        .posterPath(movie.posterPath())
        .build();
  }

  @Override
  public MovieDetailsResponse toMovieDetailsResponse(Movie movie) {
    Assert.notNull(movie, "movie must not be null");

    return MovieDetailsResponse.builder()
        .id(movie.id())
        .title(movie.title())
        .overview(movie.overview())
        .releaseDate(movie.releaseDate())
        .originalLanguage(movie.originalLanguage())
        .posterPath(movie.posterPath())
        .backdropPath(movie.backdropPath())
        .build();
  }

  @Override
  public Movie toMovie(TMDbMovieResponse tmdbMovieResponse) {
    Assert.notNull(tmdbMovieResponse, "tmdbMovieResponse must not be null");

    return Movie.builder()
        .id(tmdbMovieResponse.id())
        .title(tmdbMovieResponse.title())
        .overview(tmdbMovieResponse.overview())
        .originalLanguage(tmdbMovieResponse.originalLanguage())
        .releaseDate(tmdbMovieResponse.releaseDate())
        .posterPath(tmdbMovieResponse.posterPath())
        .backdropPath(tmdbMovieResponse.backdropPath())
        .build();
  }

}
