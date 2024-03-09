package br.com.emendes.yourreviewapi.dto;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * Record com as informações de pontuação de um Movie.
 *
 * @param id            identificador do MovieVotes.
 * @param movieId       identificador do Movie associado com o MovieVotes.
 * @param reviewTotal   valor total de reviews.
 * @param reviewAverage valor médio das reviews.
 */
@Builder
public record MovieVotesAverage(
    Long id,
    String movieId,
    long reviewTotal,
    BigDecimal reviewAverage
) {
}
