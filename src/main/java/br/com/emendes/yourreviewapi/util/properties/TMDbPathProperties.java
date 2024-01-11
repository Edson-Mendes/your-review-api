package br.com.emendes.yourreviewapi.util.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Record para capturar as properties relacionadas os paths da API do TMDb.
 *
 * @param searchMovieByName Path para busca de filmes por nome na API do TMDb.
 * @param findMovieById     Path para busca de filme por id na API DO TMDb.
 */
@ConfigurationProperties(prefix = "your-review-api.tmdb.path")
public record TMDbPathProperties(
    String searchMovieByName,
    String findMovieById
) {
}
