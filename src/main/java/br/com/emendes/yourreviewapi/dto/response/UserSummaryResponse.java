package br.com.emendes.yourreviewapi.dto.response;

import lombok.Builder;

/**
 * Record DTO com dados resumidos sobre User.
 *
 * @param id    identificador do user.
 * @param name  nome do user.
 * @param email e-mail address do user
 */
@Builder
public record UserSummaryResponse(
    Long id,
    String name,
    String email
) {
}
