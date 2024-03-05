package br.com.emendes.yourreviewapi.unit.service.impl;

import br.com.emendes.yourreviewapi.model.entity.User;
import br.com.emendes.yourreviewapi.service.impl.JWTServiceImpl;
import br.com.emendes.yourreviewapi.util.faker.AuthorityFaker;
import br.com.emendes.yourreviewapi.util.properties.JwtProperties;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Base64;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests de JWTServiceImpl")
class JWTServiceImplTest {

  @InjectMocks
  private JWTServiceImpl jwtService;
  @Mock
  private JwtProperties jwtPropertiesMock;

  @Nested
  @DisplayName("generateToken method")
  class generateTokenMethod {

    @Test
    @DisplayName("generateToken must return JWT String when generate successfully")
    void generateToken_MustReturnJWTString_WhenGenerateSuccessfully() throws JSONException {
      BDDMockito.when(jwtPropertiesMock.secret()).thenReturn("12341234123412341234123412341234");
      User userDetails = User.builder()
          .email("john.doe@email.com")
          .password("1234567890")
          .authorities(Set.of(AuthorityFaker.userAuthority()))
          .build();

      String actualJWT = jwtService.generateToken(userDetails, 1_800_000);

      assertThat(actualJWT.split("\\.")).isNotNull().hasSize(3);

      JSONObject actualPayload = extractJwtPayload(actualJWT);

      long actualIssuedAt = actualPayload.getLong("iat");
      long actualExpirationAt = actualPayload.getLong("exp");

      assertThat(actualPayload.getString("iss")).isNotNull().isEqualTo("Your Review API");
      assertThat(actualPayload.getString("sub")).isNotNull().isEqualTo("john.doe@email.com");
      assertThat(actualExpirationAt - actualIssuedAt).isEqualTo(1_800);
    }

    @Test
    @DisplayName("generateToken must throw IllegalArgumentException when UserDetails is null")
    void generateToken_MustThrowIllegalArgumentException_WhenUserDetailsIsNull() {
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> jwtService.generateToken(null, 1_800_000))
          .withMessage("userDetails must not be null");
    }

    @Test
    @DisplayName("generateToken must throw IllegalArgumentException when UserDetails.getUsername() is null")
    void generateToken_MustThrowIllegalArgumentException_WhenUserDetailsGetUsernameIsNull() {
      User userDetails = User.builder()
          .password("1234567890")
          .authorities(Set.of(AuthorityFaker.userAuthority()))
          .build();

      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> jwtService.generateToken(userDetails, 1_800_000))
          .withMessage("userDetails.username must not be null");
    }

  }

  @Nested
  @DisplayName("isTokenValid method")
  class IsTokenValidMethod {

    @Test
    @DisplayName("isTokenValid must return true when JWT is valid")
    void isTokenValid_MustReturnTrue_WhenJWTIsValid() {
      BDDMockito.when(jwtPropertiesMock.secret()).thenReturn("12341234123412341234123412341234");

      User userDetails = User.builder()
          .email("john.doe@email.com")
          .password("1234567890")
          .authorities(Set.of(AuthorityFaker.userAuthority()))
          .build();

      String jwt = jwtService.generateToken(userDetails, 1_800_000);

      boolean actualIsValid = jwtService.isTokenValid(jwt);

      assertThat(actualIsValid).isTrue();
    }

    @Test
    @DisplayName("isTokenValid must return false when jwt is malformed")
    void isTokenValid_MustReturnFalse_WhenJWTIsMalformed() {
      BDDMockito.when(jwtPropertiesMock.secret()).thenReturn("12341234123412341234123412341234");

      boolean actualIsValid = jwtService.isTokenValid("thisIsA.malformed.jwt");

      assertThat(actualIsValid).isFalse();
    }

    @Test
    @DisplayName("isTokenValid must return false when jwt is expired")
    void isTokenValid_MustReturnFalse_WhenJWTIsExpired() {
      BDDMockito.when(jwtPropertiesMock.secret()).thenReturn("12341234123412341234123412341234");

      boolean actualIsValid = jwtService.isTokenValid("eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJZb3VyIFJldmlldyBBUEkiLCJzdWIiOiJraXJhQGVtYWlsLmNvbSIsImlhdCI6MTcwOTMwOTg3OSwiZXhwIjoxNzA5Mzk2Mjc5fQ.B_X4V9TQK-_RuoGNWt0QBCtZ35JQULoiJB5JKfqQ-k8");

      assertThat(actualIsValid).isFalse();
    }

    @Test
    @DisplayName("isTokenValid must throw IllegalArgumentException when jwt is null")
    void isTokenValid_MustThrowIllegalArgumentException_WhenJWTIsNull() {
      BDDMockito.when(jwtPropertiesMock.secret()).thenReturn("12341234123412341234123412341234");

      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> jwtService.isTokenValid(null))
          .withMessage("JWT String argument cannot be null or empty.");
    }

    @Test
    @DisplayName("isTokenValid must throw IllegalArgumentException when jwt is empty")
    void isTokenValid_MustThrowIllegalArgumentException_WhenJWTIsEmpty() {
      BDDMockito.when(jwtPropertiesMock.secret()).thenReturn("12341234123412341234123412341234");

      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> jwtService.isTokenValid(""))
          .withMessage("JWT String argument cannot be null or empty.");
    }

  }

  @Nested
  @DisplayName("extractSubject method")
  class ExtractSubjectMethod {

    @Test
    @DisplayName("extractSubject must return Subject when extract successfully")
    void extractSubject_MustReturnSubject_WhenExtractSuccessfully() {
      BDDMockito.when(jwtPropertiesMock.secret()).thenReturn("12341234123412341234123412341234");

      User userDetails = User.builder()
          .email("john.doe@email.com")
          .password("1234567890")
          .authorities(Set.of(AuthorityFaker.userAuthority()))
          .build();

      String jwt = jwtService.generateToken(userDetails, 1_800_000);

      String actualSubject = jwtService.extractSubject(jwt);

      assertThat(actualSubject).isNotNull().isEqualTo("john.doe@email.com");
    }

    @Test
    @DisplayName("extractSubject must throw IllegalArgumentException when token is null")
    void extractSubject_MustThrowIllegalArgumentException_WhenTokenIsNull() {
      BDDMockito.when(jwtPropertiesMock.secret()).thenReturn("12341234123412341234123412341234");

      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> jwtService.extractSubject(null))
          .withMessage("JWT String argument cannot be null or empty.");
    }

    @Test
    @DisplayName("extractSubject must throw IllegalArgumentException when token is empty")
    void extractSubject_MustThrowIllegalArgumentException_WhenTokenIsEmpty() {
      BDDMockito.when(jwtPropertiesMock.secret()).thenReturn("12341234123412341234123412341234");

      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> jwtService.extractSubject(""))
          .withMessage("JWT String argument cannot be null or empty.");
    }

    @Test
    @DisplayName("extractSubject must throw SignatureException when secret is invalid")
    void extractSubject_MustThrowSignatureException_WhenSecretIsInvalid() {
      BDDMockito.when(jwtPropertiesMock.secret()).thenReturn("_invalid_secret__invalid_secret_");

      assertThatExceptionOfType(SignatureException.class)
          .isThrownBy(() -> jwtService.extractSubject("eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJZb3VyIFJldmlldyBBUEkiLCJzdWIiOiJqb2huLmRvZUBlbWFpbC5jb20iLCJpYXQiOjE3MDk2NTkyNzksImV4cCI6MTcwOTY2MTA3OX0.dv2kOBITnVb8HuwZnedOqkwneey6VaMXtXyPACVu84k"))
          .withMessage("JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.");
    }

    @Test
    @DisplayName("extractSubject must throw ExpiredJwtException when token is expired")
    void extractSubject_MustThrowExpiredJwtException_WhenTokenIsExpired() {
      BDDMockito.when(jwtPropertiesMock.secret()).thenReturn("12341234123412341234123412341234");

      assertThatExceptionOfType(ExpiredJwtException.class)
          .isThrownBy(() -> jwtService.extractSubject("eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJZb3VyIFJldmlldyBBUEkiLCJzdWIiOiJraXJhQGVtYWlsLmNvbSIsImlhdCI6MTcwOTMwOTg3OSwiZXhwIjoxNzA5Mzk2Mjc5fQ.B_X4V9TQK-_RuoGNWt0QBCtZ35JQULoiJB5JKfqQ-k8"));
    }

  }

  /**
   * Extrai o payload de uma String que representa um JWT.
   *
   * @param jwt objeto que representa o JWT.
   * @return JSONObject o payload do JWT.
   */
  private JSONObject extractJwtPayload(String jwt) throws JSONException {
    String[] jwtSplitted = jwt.split("\\.");

    String jsonPayload = new String(Base64.getDecoder().decode(jwtSplitted[1]));
    return new JSONObject(jsonPayload);
  }

}