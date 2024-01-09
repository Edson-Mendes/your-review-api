package br.com.emendes.yourreviewapi.client.impl;

import br.com.emendes.yourreviewapi.client.MovieClient;
import br.com.emendes.yourreviewapi.client.TMDbSearchMovieResponse;
import br.com.emendes.yourreviewapi.model.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Implementação de {@link MovieClient}.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TMDbMovieClient implements MovieClient {

  private final WebClient tmdbWebClient;
  @Value("${your-review-api.tmdb.api-key}")
  private String searchMoviePath;
  @Value("${your-review-api.tmdb.api-key}")
  private String tmdbApiKey;

  @Override
  public Page<Movie> findByName(String name, int page) {
    TMDbSearchMovieResponse response = tmdbWebClient.get().uri(uriBuilder -> uriBuilder
            .path(searchMoviePath)
            .queryParam("query", name)
            .queryParam("page", page)
            .queryParam("api_key", tmdbApiKey)
            .build())
        .retrieve().bodyToMono(TMDbSearchMovieResponse.class).block();

    return new PageImpl<>(response.getResults(), PageRequest.of(page, 20), response.getTotalResults());
  }

}
