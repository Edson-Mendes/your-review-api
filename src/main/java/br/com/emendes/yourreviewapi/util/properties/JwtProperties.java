package br.com.emendes.yourreviewapi.util.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Classe para capturar as properties relacionadas a JWT.
 */
@ConfigurationProperties(prefix = "your-review-api.jwt")
public record JwtProperties(
    String secret
) {
}
