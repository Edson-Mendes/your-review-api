package br.com.emendes.yourreviewapi.dto.request;

import br.com.emendes.yourreviewapi.validation.annotation.CustomNotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * Record DTO com os dados de atualização de review.
 *
 * @param vote    valor do voto da review do usuário.
 * @param opinion opinião sobre o filme sendo avaliado.
 */
@Builder
public record ReviewUpdateRequest(
    @Min(value = 1, message = "{ReviewUpdateRequest.vote.Min.message}")
    @Max(value = 10, message = "{ReviewUpdateRequest.vote.Max.message}")
    int vote,
    @Size(max = 500, message = "{ReviewUpdateRequest.opinion.Size.message}")
    @CustomNotBlank(message = "{ReviewUpdateRequest.opinion.CustomNotBlank.message}")
    String opinion
) {
}
