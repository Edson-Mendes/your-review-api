package br.com.emendes.yourreviewapi.unit.dto.request;

import br.com.emendes.yourreviewapi.config.ValidatorFactoryConfig;
import br.com.emendes.yourreviewapi.dto.request.UserRegisterRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
@DisplayName("Unit tests das validações do DTO UserRegisterRequest")
class UserRegisterRequestTest {

  @Autowired
  private Validator validator;

  @Nested
  @DisplayName("name validation")
  class NameValidation {

    private static final String NAME_PROPERTY = "name";

    @ParameterizedTest
    @ValueSource(strings = {
        "Yo", "Name with 150 characters long.Name with 150 characters long." +
        "Name with 150 characters long.Name with 150 characters long.Name with 150 characters long."})
    @DisplayName("Name validation must not return violations when name is valid")
    void nameValidation_MustNotReturnViolations_WhenNameIsValid(String validName) {
      assumeThat(validName.length()).isBetween(2, 150);

      UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
          .name(validName)
          .build();

      Set<ConstraintViolation<UserRegisterRequest>> actualViolations = validator
          .validateProperty(userRegisterRequest, NAME_PROPERTY);

      assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Name validation must return violations when name is blank")
    void nameValidation_MustReturnViolations_WhenNameIsBlank(String blankName) {
      UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
          .name(blankName)
          .build();

      Set<ConstraintViolation<UserRegisterRequest>> actualViolations = validator
          .validateProperty(userRegisterRequest, NAME_PROPERTY);
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      assertThat(actualViolations).isNotEmpty();
      assertThat(actualMessages).contains("name must not be blank");
    }

    @Test
    @DisplayName("Name validation must return violations when name length is less than 2")
    void nameValidation_MustReturnViolations_WhenNameLengthIsLessThan2() {
      UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
          .name("L")
          .build();

      Set<ConstraintViolation<UserRegisterRequest>> actualViolations = validator
          .validateProperty(userRegisterRequest, NAME_PROPERTY);
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      assertThat(actualViolations).isNotEmpty();
      assertThat(actualMessages).contains("name must contain between 2 and 150 characters long");
    }

    @Test
    @DisplayName("Name validation must return violations when name length is greater than 150")
    void nameValidation_MustReturnViolations_WhenNameLengthIsGreaterThan150() {
      String nameWith151Characters = "Lorem Ipsum Dolor Sit AmetLorem Ipsum Dolor Sit Amet" +
          "Lorem Ipsum Dolor Sit AmetLorem Ipsum Dolor Sit Amet" +
          "Lorem Ipsum Dolor Sit AmetLorem Ipsum Dolor Sit";
      assumeThat(nameWith151Characters.length()).isEqualTo(151);

      UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
          .name(nameWith151Characters)
          .build();

      Set<ConstraintViolation<UserRegisterRequest>> actualViolations = validator
          .validateProperty(userRegisterRequest, NAME_PROPERTY);
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      assertThat(actualViolations).isNotEmpty();
      assertThat(actualMessages).contains("name must contain between 2 and 150 characters long");
    }

  }

  @Nested
  @DisplayName("email validation")
  class EmailValidation {

    private static final String EMAIL_PROPERTY = "email";

    @ParameterizedTest
    @ValueSource(strings = {
        "lorem@email.com", "loremloremloremloremloremloremloremloremloremloremloremloremlore@" +
        "emailemailemailemailemailemailemailemailemailemailemailemailema." +
        "emailemailemailemailemailemailemailemailemailemailemailemailema." +
        "emailemailemailemailemailemailemailemailemailemailemailemailema." +
        "emailemailemailemailemailemailemailemailemailemailemailemailema"
    })
    @DisplayName("Email validation must not return violations when email is valid")
    void emailValidation_MustNotReturnViolations_WhenEmailIsValid(String validEmail) {
      assumeThat(validEmail.length()).isLessThanOrEqualTo(320);

      UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
          .email(validEmail)
          .build();

      Set<ConstraintViolation<UserRegisterRequest>> actualViolations = validator
          .validateProperty(userRegisterRequest, EMAIL_PROPERTY);

      assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Email validation must return violations when email is blank")
    void emailValidation_MustReturnViolations_WhenEmailIsBlank(String blankEmail) {
      UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
          .email(blankEmail)
          .build();

      Set<ConstraintViolation<UserRegisterRequest>> actualViolations = validator
          .validateProperty(userRegisterRequest, EMAIL_PROPERTY);
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      assertThat(actualViolations).isNotEmpty();
      assertThat(actualMessages).contains("email must not be blank");
    }

    @Test
    @DisplayName("Email validation must return violations when email length is greater than 320")
    void emailValidation_MustReturnViolations_WhenEmailLengthIsGreaterThan150() {
      String emailWith321Characters = "loremloremloremloremloremloremloremloremloremloremloremloremlore@" +
          "emailemailemailemailemailemailemailemailemailemailemailemailema." +
          "emailemailemailemailemailemailemailemailemailemailemailemailema." +
          "emailemailemailemailemailemailemailemailemailemailemailemailema." +
          "emailemailemailemailemailemailemailemailemailemailemailemailemai";
      assumeThat(emailWith321Characters.length()).isEqualTo(321);

      UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
          .email(emailWith321Characters)
          .build();

      Set<ConstraintViolation<UserRegisterRequest>> actualViolations = validator
          .validateProperty(userRegisterRequest, EMAIL_PROPERTY);
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      assertThat(actualViolations).isNotEmpty();
      assertThat(actualMessages).contains("email must contain max 320 characters long");
    }

    @ParameterizedTest
    @ValueSource(strings = {"lorem", "lorem@", "lorem.com", "@email.com"})
    @DisplayName("Email validation must return violations when email is not well-formed")
    void emailValidation_MustReturnViolations_WhenEmailIsNotWellFormed(String emailNotWellFormed) {
      UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
          .email(emailNotWellFormed)
          .build();

      Set<ConstraintViolation<UserRegisterRequest>> actualViolations = validator
          .validateProperty(userRegisterRequest, EMAIL_PROPERTY);
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      assertThat(actualViolations).isNotEmpty();
      assertThat(actualMessages).contains("email must be a well-formed E-mail address");
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

      UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
          .password(validPassword)
          .build();

      Set<ConstraintViolation<UserRegisterRequest>> actualViolations = validator
          .validateProperty(userRegisterRequest, PASSWORD_PROPERTY);

      assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Password validation must return violations when password is blank")
    void passwordValidation_MustReturnViolations_WhenPasswordIsBlank(String blankPassword) {
      UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
          .password(blankPassword)
          .build();

      Set<ConstraintViolation<UserRegisterRequest>> actualViolations = validator
          .validateProperty(userRegisterRequest, PASSWORD_PROPERTY);
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      assertThat(actualViolations).isNotEmpty();
      assertThat(actualMessages).contains("password must not be blank");
    }

    @Test
    @DisplayName("Password validation must return violations when password length is less than 8")
    void passwordValidation_MustReturnViolations_WhenPasswordLengthIsLessThan8() {
      UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
          .password("1234567")
          .build();

      Set<ConstraintViolation<UserRegisterRequest>> actualViolations = validator
          .validateProperty(userRegisterRequest, PASSWORD_PROPERTY);
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      assertThat(actualViolations).isNotEmpty();
      assertThat(actualMessages).contains("password must contain between 8 and 30 characters long");
    }

    @Test
    @DisplayName("Password validation must return violations when password length is greater than 30")
    void passwordValidation_MustReturnViolations_WhenPasswordLengthIsGreaterThan30() {
      String passwordWith30Characters = "1234567890abcdefghijklmnopqrstu";
      assumeThat(passwordWith30Characters.length()).isEqualTo(31);

      UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
          .password(passwordWith30Characters)
          .build();

      Set<ConstraintViolation<UserRegisterRequest>> actualViolations = validator
          .validateProperty(userRegisterRequest, PASSWORD_PROPERTY);
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      assertThat(actualViolations).isNotEmpty();
      assertThat(actualMessages).contains("password must contain between 8 and 30 characters long");
    }

  }

  @Nested
  @DisplayName("confirmPassword validation")
  class ConfirmPasswordValidation {

    private static final String CONFIRM_PASSWORD_PROPERTY = "confirmPassword";

    @ParameterizedTest
    @ValueSource(strings = {
        "12345678", "1234567890abcdefghijklmnopqrst"})
    void confirmPasswordValidation_MustNotReturnViolations_WhenConfirmPasswordIsValid(String validConfirmPassword) {
      UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
          .confirmPassword(validConfirmPassword)
          .build();

      Set<ConstraintViolation<UserRegisterRequest>> actualViolations = validator
          .validateProperty(userRegisterRequest, CONFIRM_PASSWORD_PROPERTY);

      assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("ConfirmPassword validation must return violations when confirmPassword is blank")
    void confirmPasswordValidation_MustReturnViolations_WhenConfirmPasswordIsBlank(String blankConfirmPassword) {
      UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
          .confirmPassword(blankConfirmPassword)
          .build();

      Set<ConstraintViolation<UserRegisterRequest>> actualViolations = validator
          .validateProperty(userRegisterRequest, CONFIRM_PASSWORD_PROPERTY);
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      assertThat(actualViolations).isNotEmpty();
      assertThat(actualMessages).contains("confirmPassword must not be blank");
    }

  }

}