package br.com.emendes.yourreviewapi.unit.service.impl;

import br.com.emendes.yourreviewapi.model.entity.User;
import br.com.emendes.yourreviewapi.service.impl.JWTServiceImpl;
import br.com.emendes.yourreviewapi.util.faker.AuthorityFaker;
import br.com.emendes.yourreviewapi.util.properties.JwtProperties;
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
    void generateToken_MustReturnJWTString_WhenGenerateSuccessfully() throws JSONException {
      BDDMockito.when(jwtPropertiesMock.secret()).thenReturn("12341234123412341234123412341234");
      User userDetails = User.builder()
          .email("lorem@email.com")
          .password("1234567890")
          .authorities(Set.of(AuthorityFaker.userAuthority()))
          .build();

      String actualJWT = jwtService.generateToken(userDetails, 1_800_000);

      assertThat(actualJWT.split("\\.")).isNotNull().hasSize(3);

      JSONObject actualPayload = extractJwtPayload(actualJWT);

      long actualIssuedAt = actualPayload.getLong("iat");
      long actualExpirationAt = actualPayload.getLong("exp");

      assertThat(actualPayload.getString("iss")).isNotNull().isEqualTo("Your Review API");
      assertThat(actualPayload.getString("sub")).isNotNull().isEqualTo("lorem@email.com");
      assertThat(actualExpirationAt - actualIssuedAt).isEqualTo(1_800);
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