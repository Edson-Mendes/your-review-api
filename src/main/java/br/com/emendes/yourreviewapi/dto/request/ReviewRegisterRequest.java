package br.com.emendes.yourreviewapi.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * Record DTO com os dados de registro de review.
 *
 * @param vote    valor do voto da review do usuário.
 * @param opinion opinião sobre o filme sendo avaliado.
 * @param movieId identificador do filme que receberá a avaliação
 */
@Builder
public record ReviewRegisterRequest(
    @Min(value = 1, message = "{review.vote.min}")
    @Max(value = 10, message = "{review.vote.max}")
    int vote,
    @Size(max = 500, message = "{review.opinion.size}")
    String opinion,
    @NotBlank(message = "{review.movieid.notblank}")
    String movieId
) {
}
