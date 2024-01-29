package br.com.emendes.yourreviewapi.dto.response;

import lombok.Builder;

/**
 * Record DTO com JWT que comprova a autenticidade do usuário.
 *
 * @param token JWT que comprova a autenticidade do usuário.
 * @param type  tipo do token (normalmente Bearer)
 */
@Builder
public record AuthenticationResponse(
    String token,
    String type
) {
}
