package br.com.emendes.yourreviewapi.repository.projection;

import lombok.Builder;

/**
 * Record projection usado para buscar apenas os dados necessários para resumir um User.
 *
 * @param id    identificador do usuário.
 * @param name  nome do usuário
 * @param email endereço de E-mail do usuário
 */
@Builder
public record UserSummaryProjection(
    Long id,
    String name,
    String email
) {
}
