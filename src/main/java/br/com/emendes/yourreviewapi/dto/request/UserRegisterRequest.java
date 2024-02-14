package br.com.emendes.yourreviewapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * Record DTO com os dados de registro de usuário.
 *
 * @param name            nome do usuário.
 * @param email           endereço de E-mail do usuário.
 * @param password        senha do usuário
 * @param confirmPassword confirmação de senha do usuário.
 */
@Builder
public record UserRegisterRequest(
    @NotBlank(message = "{UserRegisterRequest.name.NotBlank.message}")
    @Size(min = 2, max = 150, message = "{UserRegisterRequest.name.Size.message}")
    String name,
    @NotBlank(message = "{UserRegisterRequest.email.NotBlank.message}")
    @Size(max = 320, message = "{UserRegisterRequest.email.Size.message}")
    @Email(message = "{UserRegisterRequest.email.Email.message}")
    String email,
    @NotBlank(message = "{UserRegisterRequest.password.NotBlank.message}")
    @Size(min = 8, max = 30, message = "{UserRegisterRequest.password.Size.message}")
    String password,
    @NotBlank(message = "{UserRegisterRequest.confirmPassword.NotBlank.message}")
    String confirmPassword
) {
}
