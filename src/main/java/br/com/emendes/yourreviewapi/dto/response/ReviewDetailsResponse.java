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
 * @param movie     informações resumidas do Movie relacionado a review.
 * @param createdAt data de criação da review.
 */
@Builder
public record ReviewDetailsResponse(
    Long id,
    int vote,
    String opinion,
    Long userId,
    MovieSummaryResponse movie,
    LocalDateTime createdAt
) {
}
