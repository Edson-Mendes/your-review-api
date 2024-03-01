package br.com.emendes.yourreviewapi.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Record DTO com dados detalhados sobre Review.
 *
 * @param id        identificador da Review
 * @param vote      valor do voto do filme avaliado.
 * @param opinion   opinião do usuário sobre o filme.
 * @param userId    identificador do usuário que fez esta Review.
 * @param movieId   idenficador do filme o qual a review se aplica.
 * @param createdAt data de criação da review.
 */
@Builder
public record ReviewResponse(
    Long id,
    int vote,
    String opinion,
    Long userId,
    String movieId,
    LocalDateTime createdAt
) {
}
