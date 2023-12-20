package br.com.emendes.yourreviewapi.dto.response;

import lombok.Builder;

/**
 * Record DTO com JWT que comprova a autenticidade do usuário.
 */
@Builder
public record AuthenticationResponse(
    String token,
    String type
) {
}
