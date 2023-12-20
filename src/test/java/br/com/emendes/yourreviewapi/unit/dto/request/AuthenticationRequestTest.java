package br.com.emendes.yourreviewapi.unit.dto.request;

import br.com.emendes.yourreviewapi.config.ValidatorFactoryConfig;
import br.com.emendes.yourreviewapi.dto.request.AuthenticationRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ValidatorFactoryConfig.class})
@DisplayName("Unit tests das validações do DTO AuthenticationRequest")
class AuthenticationRequestTest {

  @Autowired
  private Validator validator;

  @Nested
  @DisplayName("username validation")
  class UsernameValidation {

    private static final String USERNAME_PROPERTY = "username";

    @ParameterizedTest
    @ValueSource(strings = {
        "lorem@email.com", "loremloremloremloremloremloremloremloremloremloremloremloremlore@" +
        "emailemailemailemailemailemailemailemailemailemailemailemailema." +
        "emailemailemailemailemailemailemailemailemailemailemailemailema." +
        "emailemailemailemailemailemailemailemailemailemailemailemailema." +
        "emailemailemailemailemailemailemailemailemailemailemailemailema"
    })
    @DisplayName("Username validation must not return violations when username is valid")
    void usernameValidation_MustNotReturnViolations_WhenUsernameIsValid(String validUsername) {
      assumeThat(validUsername.length()).isLessThanOrEqualTo(320);

      AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
          .username(validUsername)
          .build();

      Set<ConstraintViolation<AuthenticationRequest>> actualViolations = validator
          .validateProperty(authenticationRequest, USERNAME_PROPERTY);

      assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Username validation must return violations when username is blank")
    void usernameValidation_MustReturnViolations_WhenUsernameIsBlank(String blankUsername) {
      AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
          .username(blankUsername)
          .build();

      Set<ConstraintViolation<AuthenticationRequest>> actualViolations = validator
          .validateProperty(authenticationRequest, USERNAME_PROPERTY);
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      assertThat(actualViolations).isNotEmpty();
      assertThat(actualMessages).contains("username must not be blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"lorem", "lorem@", "lorem.com", "@email.com"})
    @DisplayName("Username validation must return violations when username is not well-formed")
    void usernameValidation_MustReturnViolations_WhenUsernameIsNotWellFormed(String usernameNotWellFormed) {
      AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
          .username(usernameNotWellFormed)
          .build();

      Set<ConstraintViolation<AuthenticationRequest>> actualViolations = validator
          .validateProperty(authenticationRequest, USERNAME_PROPERTY);
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      assertThat(actualViolations).isNotEmpty();
      assertThat(actualMessages).contains("username must be a well-formed E-mail address");
    }

  }

  @Nested
  @DisplayName("password validation")
  class PasswordValidation {

    private static final String PASSWORD_PROPERTY = "password";

    @ParameterizedTest
    @ValueSource(strings = {
        "12345678", "1234567890abcdefghijklmnopqrst"})
    void passwordValidation_MustNotReturnViolations_WhenPasswordIsValid(String validPassword) {
      assumeThat(validPassword.length()).isBetween(8, 30);

      AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
          .password(validPassword)
          .build();

      Set<ConstraintViolation<AuthenticationRequest>> actualViolations = validator
          .validateProperty(authenticationRequest, PASSWORD_PROPERTY);

      assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Password validation must return violations when password is blank")
    void passwordValidation_MustReturnViolations_WhenPasswordIsBlank(String blankPassword) {
      AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
          .password(blankPassword)
          .build();

      Set<ConstraintViolation<AuthenticationRequest>> actualViolations = validator
          .validateProperty(authenticationRequest, PASSWORD_PROPERTY);
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      assertThat(actualViolations).isNotEmpty();
      assertThat(actualMessages).contains("password must not be blank");
    }

  }

}