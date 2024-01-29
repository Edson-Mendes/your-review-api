package br.com.emendes.yourreviewapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * Record DTO com os dados de autenticação do usuário.
 *
 * @param username username do usuário para autenticação.
 * @param password senha do usuário para autenticação.
 */
@Builder
public record AuthenticationRequest(
    @NotBlank(message = "{authentication.username.notblank}")
    @Email(message = "{authentication.username.email}")
    String username,
    @NotBlank(message = "{authentication.password.notblank}")
    String password
) {
}
