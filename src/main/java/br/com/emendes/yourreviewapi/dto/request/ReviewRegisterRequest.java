package br.com.emendes.yourreviewapi.dto.request;

import br.com.emendes.yourreviewapi.validation.annotation.CustomNotBlank;
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
    @Min(value = 1, message = "{ReviewRegisterRequest.vote.Min.message}")
    @Max(value = 10, message = "{ReviewRegisterRequest.vote.Max.message}")
    int vote,
    @Size(max = 500, message = "{ReviewRegisterRequest.opinion.Size.message}")
    @CustomNotBlank(message = "{ReviewRegisterRequest.opinion.CustomNotBlank.message}")
    String opinion,
    @NotBlank(message = "{ReviewRegisterRequest.movieId.NotBlank.message}")
    String movieId
) {
}
