package br.com.emendes.yourreviewapi.mapper.impl;

import br.com.emendes.yourreviewapi.dto.response.MovieSummaryResponse;
import br.com.emendes.yourreviewapi.mapper.MovieMapper;
import br.com.emendes.yourreviewapi.model.Movie;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.net.URI;

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
        .originalTitle(movie.title())
        .releaseDate(movie.releaseDate())
        .posterPath(movie.posterPath())
        .build();
  }

}
