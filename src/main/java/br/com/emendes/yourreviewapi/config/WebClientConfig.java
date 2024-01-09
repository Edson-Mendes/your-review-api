package br.com.emendes.yourreviewapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3";

  @Bean
  public WebClient tmdbWebClient() {
    return WebClient.builder().baseUrl(TMDB_BASE_URL)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }

}
