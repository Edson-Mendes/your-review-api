package br.com.emendes.yourreviewapi.client.impl;

import br.com.emendes.yourreviewapi.client.MovieClient;
import br.com.emendes.yourreviewapi.dto.response.TMDbMovieResponse;
import br.com.emendes.yourreviewapi.dto.response.TMDbSearchMovieResponse;
import br.com.emendes.yourreviewapi.exception.InvalidTMDbApiKeyException;
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
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

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
    if (page < 0) throw new IllegalArgumentException("page must not be negative");

    TMDbSearchMovieResponse response = tmdbWebClient.get().uri(uriBuilder -> uriBuilder
            .path(tmdbPath.searchMovieByName())
            .queryParam("query", name)
            .queryParam("page", page + 1) //A paginação do TMDb API começa em 1.
            .queryParam(API_KEY_PARAM_NAME, tmdbApiKey)
            .build())
        .retrieve()
        .onStatus(httpStatusCode -> httpStatusCode.value() == 401, handleUnauthorizedStatusCode())
        .bodyToMono(TMDbSearchMovieResponse.class).block();

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
        .onStatus(httpStatusCode -> httpStatusCode.value() == 401, handleUnauthorizedStatusCode())
        .bodyToMono(TMDbMovieResponse.class).block();

    return movieMapper.toMovie(tmDbMovieResponse);
  }

  /**
   * handle para quando houver response status 401 (Unauthorized).
   */
  private static Function<ClientResponse, Mono<? extends Throwable>> handleUnauthorizedStatusCode() {
    return response -> {
      String errorMessage = "invalid TMDb API Key";
      log.info(errorMessage);
      return Mono.error(new InvalidTMDbApiKeyException("invalid TMDb API Key"));
    };
  }

}
