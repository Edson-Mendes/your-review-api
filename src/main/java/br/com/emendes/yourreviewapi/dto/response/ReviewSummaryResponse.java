package br.com.emendes.yourreviewapi.dto.response;

import lombok.Builder;

/**
 * Record DTO com dados resumidos sobre Review.
 *
 * @param id      identificador da Review
 * @param vote    valor do voto do filme avaliado.
 * @param opinion opinião do usuário sobre o filme.
 * @param userId  identificador do usuário que fez está Review.
 * @param movieId idenficador do filme o qual a review se aplica.
 */
@Builder
public record ReviewSummaryResponse(
    Long id,
    int vote,
    String opinion,
    UserSummaryResponse user,
    String movieId
) {
}
