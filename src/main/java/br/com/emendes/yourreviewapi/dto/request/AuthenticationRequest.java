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
    @NotBlank(message = "{AuthenticationRequest.username.NotBlank.message}")
    @Email(message = "{AuthenticationRequest.username.Email.message}")
    String username,
    @NotBlank(message = "{AuthenticationRequest.password.NotBlank.message}")
    String password
) {
}
