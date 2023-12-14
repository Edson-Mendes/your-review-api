package br.com.emendes.yourreviewapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Record DTO com os dados de registro de usuário.
 */
public record UserRegisterRequest(
    @NotBlank(message = "{userregister.name.notblank}")
    @Size(min = 2, max = 150, message = "{userregister.name.size}")
    String name,
    @NotBlank(message = "{userregister.email.notblank}")
    @Size(max = 320, message = "{userregister.email.size}")
    @Email(message = "{userregister.email.email}")
    String email,
    @NotBlank(message = "{userregister.password.notblank}")
    @Size(min = 8, max = 30, message = "{userregister.password.notblank}")
    String password,
    @NotBlank(message = "{userregister.confirmpassword.notblank}")
    String confirmPassword
) {
}
