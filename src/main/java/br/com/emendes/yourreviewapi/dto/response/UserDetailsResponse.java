package br.com.emendes.yourreviewapi.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Record DTO com dados resumidos sobre User.
 *
 * @param id        identificador do usuário.
 * @param name      nome do usuário.
 * @param email     endereço de E-mai do usuário.
 * @param status    status do usuário no sistema.
 * @param createdAt data de registro do usuário no sistema.
 */
@Builder
public record UserDetailsResponse(
    Long id,
    String name,
    String email,
    String status,
    LocalDateTime createdAt
) {
}
