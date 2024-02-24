package br.com.emendes.yourreviewapi.integration.controller;

import br.com.emendes.yourreviewapi.controller.AuthenticationController;
import br.com.emendes.yourreviewapi.dto.response.AuthenticationResponse;
import br.com.emendes.yourreviewapi.exception.UserIsNotAuthenticatedException;
import br.com.emendes.yourreviewapi.service.AuthenticationService;
import br.com.emendes.yourreviewapi.service.JWTService;
import br.com.emendes.yourreviewapi.util.component.AuthenticatedUserComponent;
import br.com.emendes.yourreviewapi.util.faker.UserFaker;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"controller-it"})
@WebMvcTest(
    excludeAutoConfiguration = {RedisAutoConfiguration.class, SecurityAutoConfiguration.class},
    useDefaultFilters = false,
    includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
            AuthenticationService.class,
            AuthenticationController.class
        }),
        @ComponentScan.Filter(classes = {RestControllerAdvice.class})
    }
)
@DisplayName("Test dos endpoints de AuthenticationController")
public class AuthenticationControllerIT {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper mapper;
  @MockBean
  private AuthenticationManager authenticationManagerMock;
  @MockBean
  private JWTService jwtServiceMock;
  @MockBean
  private AuthenticatedUserComponent authenticatedUserComponentMock;

  private final String CONTENT_TYPE = "application/json;charset=UTF-8";

  @Nested
  @DisplayName("Tests for signIn endpoint")
  class SignInEndpoint {

    @Mock
    private Authentication authenticationMock;

    private static final String SIGN_IN_URI = "/api/v1/auth/signin";

    @Test
    @DisplayName("POST /api/v1/auth/signin must return status 200 when sign in successfully")
    void signIn_MustReturnStatus200_WhenSignInSuccessfully() throws Exception {
      when(authenticationManagerMock.authenticate(any())).thenReturn(authenticationMock);
      when(authenticationMock.getPrincipal()).thenReturn(UserFaker.user());
      when(jwtServiceMock.generateToken(any(), eq(1_800_000L))).thenReturn("token.generated");
      String requestBody = """
          {
            "username": "lorem@email.com",
            "password": "1234567890"
          }
          """;

      mockMvc.perform(post(SIGN_IN_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/v1/auth/signin must return AuthenticationResponse when sign in successfully")
    void signIn_MustReturnAuthenticationResponse_WhenSignInSuccessfully() throws Exception {
      when(authenticationManagerMock.authenticate(any())).thenReturn(authenticationMock);
      when(authenticationMock.getPrincipal()).thenReturn(UserFaker.user());
      when(jwtServiceMock.generateToken(any(), eq(1_800_000L))).thenReturn("token.generated");
      String requestBody = """
          {
            "username": "lorem@email.com",
            "password": "1234567890"
          }
          """;

      String actualContent = mockMvc.perform(post(SIGN_IN_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();

      AuthenticationResponse actualResponseBody = mapper.readValue(actualContent, AuthenticationResponse.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.type()).isNotNull().isEqualTo("Bearer");
      assertThat(actualResponseBody.token()).isNotNull().isEqualTo("token.generated");
    }

    @Test
    @DisplayName("POST /api/v1/auth/signin must return status 400 when body has invalid fields")
    void signIn_MustReturnStatus400_WhenWhenBodyHasInvalidFields() throws Exception {
      String requestBody = """
          {
            "username": "",
            "password": "1234567890"
          }
          """;

      mockMvc.perform(post(SIGN_IN_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/auth/signin must return ProblemDetail when body has invalid fields")
    void signIn_MustReturnProblemDetail_WhenWhenBodyHasInvalidFields() throws Exception {
      String requestBody = """
          {
            "username": "",
            "password": "1234567890"
          }
          """;

      String actualContent = mockMvc.perform(post(SIGN_IN_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Bad request");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("Some fields are invalids");
      assertThat(actualResponseBody.getStatus()).isEqualTo(400);
      assertThat(actualResponseBody.getProperties()).isNotNull();
      assertThat(actualResponseBody.getProperties().get("fields")).isNotNull().isEqualTo("username");
      assertThat(actualResponseBody.getProperties().get("messages")).isNotNull()
          .isEqualTo("username must not be blank");
    }

    @Test
    @DisplayName("POST /api/v1/auth/signin must return status 400 when username is wrong")
    void signIn_MustReturnStatus400_WhenUsernameIsWrong() throws Exception {
      when(authenticationManagerMock.authenticate(any()))
          .thenThrow(new BadCredentialsException("Bad credentials"));

      String requestBody = """
          {
            "username": "wrong.email@email.com",
            "password": "1234567890"
          }
          """;

      mockMvc.perform(post(SIGN_IN_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/auth/signin must return ProblemDetail when username or password are wrong")
    void signIn_MustReturnProblemDetail_WhenUsernameOrPasswordAreWrong() throws Exception {
      when(authenticationManagerMock.authenticate(any()))
          .thenThrow(new BadCredentialsException("Bad credentials"));

      String requestBody = """
          {
            "username": "wrong.email@email.com",
            "password": "1234567890"
          }
          """;

      String actualContent = mockMvc.perform(post(SIGN_IN_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Bad credentials");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("wrong email or password");
      assertThat(actualResponseBody.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("POST /api/v1/auth/signin must return status 400 when user is locked")
    void signIn_MustReturnStatus400_WhenUserIsLocked() throws Exception {
      when(authenticationManagerMock.authenticate(any()))
          .thenThrow(new LockedException("User account is locked"));

      String requestBody = """
          {
            "username": "blocked.email@email.com",
            "password": "1234567890"
          }
          """;

      mockMvc.perform(post(SIGN_IN_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/auth/signin must return ProblemDetail when user is locked")
    void signIn_MustReturnProblemDetail_WhenUserIsLocked() throws Exception {
      when(authenticationManagerMock.authenticate(any()))
          .thenThrow(new LockedException("User account is locked"));

      String requestBody = """
          {
            "username": "blocked.email@email.com",
            "password": "1234567890"
          }
          """;

      String actualContent = mockMvc.perform(post(SIGN_IN_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Bad request");
      assertThat(actualResponseBody.getDetail()).isNotNull()
          .isEqualTo("User account is locked, check your email for activate your account");
      assertThat(actualResponseBody.getStatus()).isEqualTo(400);
    }

  }

  @Nested
  @DisplayName("Tests for refreshToken endpoint")
  class RefreshTokenEndpoint {

    private static final String REFRESH_TOKEN_URI = "/api/v1/auth/refresh";

    @Test
    @DisplayName("GET /api/v1/auth/refresh must return status 200 when refresh successfully")
    void refreshToken_MustReturnStatus200_WhenRefreshSuccessfully() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(jwtServiceMock.generateToken(any(), eq(1_800_000L))).thenReturn("token.refreshed");

      mockMvc.perform(get(REFRESH_TOKEN_URI).contentType(CONTENT_TYPE)).andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/auth/refresh must return AuthenticationResponse when refresh successfully")
    void refreshToken_MustReturnAuthenticationResponse_WhenRefreshSuccessfully() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser()).thenReturn(UserFaker.user());
      when(jwtServiceMock.generateToken(any(), eq(1_800_000L))).thenReturn("token.refreshed");

      String actualContent = mockMvc.perform(get(REFRESH_TOKEN_URI).contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();

      AuthenticationResponse actualResponseBody = mapper.readValue(actualContent, AuthenticationResponse.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.type()).isNotNull().isEqualTo("Bearer");
      assertThat(actualResponseBody.token()).isNotNull().isEqualTo("token.refreshed");
    }

    @Test
    @DisplayName("GET /api/v1/auth/refresh must return status 500 when user is not authenticated")
    void refreshToken_MustReturnStatus400_WhenUserIsNotAuthenticated() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser())
          .thenThrow(new UserIsNotAuthenticatedException("User is not authenticate"));

      mockMvc.perform(get(REFRESH_TOKEN_URI).contentType(CONTENT_TYPE))
          .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GET /api/v1/auth/refresh must return ProblemDetail when user is not authenticated")
    void refreshToken_MustReturnProblemDetail_WhenUserIsNotAuthenticated() throws Exception {
      when(authenticatedUserComponentMock.getCurrentUser())
          .thenThrow(new UserIsNotAuthenticatedException("User is not authenticate"));

      String actualContent = mockMvc.perform(get(REFRESH_TOKEN_URI).contentType(CONTENT_TYPE))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Internal server error");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("User is not authenticate");
      assertThat(actualResponseBody.getStatus()).isEqualTo(500);
    }

  }

}
