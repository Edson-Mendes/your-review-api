package br.com.emendes.yourreviewapi.client.impl;

import br.com.emendes.yourreviewapi.client.MovieClient;
import br.com.emendes.yourreviewapi.dto.response.TMDbMovieResponse;
import br.com.emendes.yourreviewapi.dto.response.TMDbSearchMovieResponse;
import br.com.emendes.yourreviewapi.exception.MovieNotFoundException;
import br.com.emendes.yourreviewapi.mapper.MovieMapper;
import br.com.emendes.yourreviewapi.model.Movie;
import br.com.emendes.yourreviewapi.util.properties.TMDbPathProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Implementação de {@link MovieClient}.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TMDbMovieClient implements MovieClient {

  public static final String API_KEY_PARAM_NAME = "api_key";

  private final WebClient tmdbWebClient;
  private final TMDbPathProperties tmdbPath;
  private final MovieMapper movieMapper;
  @Value("${your-review-api.tmdb.api-key}")
  private String tmdbApiKey;

  @Override
  public Page<Movie> findByName(String name, int page) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException("name must not be null or blank");
    if (page < 1) throw new IllegalArgumentException("page must not be less than 1");

    TMDbSearchMovieResponse response = tmdbWebClient.get().uri(uriBuilder -> uriBuilder
            .path(tmdbPath.searchMovieByName())
            .queryParam("query", name)
            .queryParam("page", page)
            .queryParam(API_KEY_PARAM_NAME, tmdbApiKey)
            .build())
        .retrieve().bodyToMono(TMDbSearchMovieResponse.class).block();

    assert response != null;
    List<Movie> movies = response.getResults().stream().map(movieMapper::toMovie).toList();

    return new PageImpl<>(movies, PageRequest.of(page, 20), response.getTotalResults());
  }

  @Override
  public Movie findById(String movieId) {
    TMDbMovieResponse tmDbMovieResponse = tmdbWebClient.get().uri(uriBuilder -> uriBuilder
            .path(tmdbPath.findMovieById())
            .queryParam(API_KEY_PARAM_NAME, tmdbApiKey)
            .build(movieId))
        .retrieve()
        .onStatus(
            httpStatusCode -> httpStatusCode.value() == 404,
            response -> {
              String errorMessage = "movie not found with id: %s".formatted(movieId);
              log.info(errorMessage);
              return Mono.error(new MovieNotFoundException(errorMessage));
            })
        .bodyToMono(TMDbMovieResponse.class).block();

    return movieMapper.toMovie(tmDbMovieResponse);
  }

}
