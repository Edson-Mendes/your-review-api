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
    @NotBlank(message = "{userregister.name.notblank}")
    @Size(min = 2, max = 150, message = "{userregister.name.size}")
    String name,
    @NotBlank(message = "{userregister.email.notblank}")
    @Size(max = 320, message = "{userregister.email.size}")
    @Email(message = "{userregister.email.email}")
    String email,
    @NotBlank(message = "{userregister.password.notblank}")
    @Size(min = 8, max = 30, message = "{userregister.password.size}")
    String password,
    @NotBlank(message = "{userregister.confirmpassword.notblank}")
    String confirmPassword
) {
}
