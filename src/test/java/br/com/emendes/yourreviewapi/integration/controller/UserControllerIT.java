package br.com.emendes.yourreviewapi.integration.controller;

import br.com.emendes.yourreviewapi.controller.UserController;
import br.com.emendes.yourreviewapi.dto.response.UserDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.UserSummaryResponse;
import br.com.emendes.yourreviewapi.mapper.UserMapper;
import br.com.emendes.yourreviewapi.repository.UserRepository;
import br.com.emendes.yourreviewapi.service.AuthorityService;
import br.com.emendes.yourreviewapi.service.UserService;
import br.com.emendes.yourreviewapi.util.PageableResponse;
import br.com.emendes.yourreviewapi.util.faker.AuthorityFaker;
import br.com.emendes.yourreviewapi.util.faker.UserFaker;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ProblemDetail;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    excludeAutoConfiguration = {RedisAutoConfiguration.class, SecurityAutoConfiguration.class},
    useDefaultFilters = false,
    includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {UserController.class, UserService.class}),
        @ComponentScan.Filter(classes = {RestControllerAdvice.class})
    }
)
@DisplayName("Test dos endpoints de UserController")
class UserControllerIT {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper mapper;
  @MockBean
  private AuthorityService authorityServiceMock;
  @MockBean
  private UserRepository userRepositoryMock;
  @MockBean
  private UserMapper userMapperMock;
  @MockBean
  private PasswordEncoder passwordEncoderMock;

  private final String CONTENT_TYPE = "application/json;charset=UTF-8";

  @Nested
  @DisplayName("Tests for register endpoint")
  class RegisterEndpoint {

    private static final String REGISTER_URI = "/api/v1/users";

    @Test
    @DisplayName("POST /api/v1/users must return status 201 when register user successfully")
    void register_MustReturnStatus201_WhenRegisterUserSuccessfully() throws Exception {
      when(userMapperMock.toUser(any())).thenReturn(UserFaker.userToBeRegistered());
      when(authorityServiceMock.findByName("USER")).thenReturn(AuthorityFaker.userAuthority());
      when(passwordEncoderMock.encode("1234567890")).thenReturn("encoded-password");
      when(userRepositoryMock.save(any())).thenReturn(UserFaker.user());
      when(userMapperMock.toUserDetailsResponse(any())).thenReturn(UserFaker.userDetailsResponse());

      String requestBody = """
          {
            "name": "John Doe",
            "email": "john.doe@email.com",
            "password": "1234567890",
            "confirmPassword": "1234567890"
          }
          """;

      mockMvc.perform(post(REGISTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /api/v1/users must return UserDetailsResponse when register user successfully")
    void register_MustReturnUserDetailsResponse_WhenRegisterUserSuccessfully() throws Exception {
      when(userMapperMock.toUser(any())).thenReturn(UserFaker.userToBeRegistered());
      when(authorityServiceMock.findByName("USER")).thenReturn(AuthorityFaker.userAuthority());
      when(passwordEncoderMock.encode("1234567890")).thenReturn("encoded-password");
      when(userRepositoryMock.save(any())).thenReturn(UserFaker.user());
      when(userMapperMock.toUserDetailsResponse(any())).thenReturn(UserFaker.userDetailsResponse());

      String requestBody = """
          {
            "name": "John Doe",
            "email": "john.doe@email.com",
            "password": "1234567890",
            "confirmPassword": "1234567890"
          }
          """;

      String actualContent = mockMvc.perform(post(REGISTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();

      UserDetailsResponse actualResponseBody = mapper.readValue(actualContent, UserDetailsResponse.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.id()).isNotNull().isEqualTo(100L);
      assertThat(actualResponseBody.name()).isNotNull().isEqualTo("John Doe");
      assertThat(actualResponseBody.email()).isNotNull().isEqualTo("john.doe@email.com");
      assertThat(actualResponseBody.createdAt()).isNotNull();
    }

    @Test
    @DisplayName("POST /api/v1/users must return status 400 when request body has invalid fields")
    void register_MustReturnStatus400_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
          {
            "name": "",
            "email": "john.doe@email.com",
            "password": "1234567890",
            "confirmPassword": "1234567890"
          }
          """;

      mockMvc.perform(post(REGISTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/users must return ProblemDetail when request body has invalid fields")
    void register_MustReturnProblemDetail_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
          {
            "name": "",
            "email": "john.doe@email.com",
            "password": "1234567890",
            "confirmPassword": "1234567890"
          }
          """;

      String actualContent = mockMvc.perform(post(REGISTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Bad request");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("Some fields are invalids");
      assertThat(actualResponseBody.getStatus()).isEqualTo(400);
      assertThat(actualResponseBody.getProperties()).isNotNull();
      assertThat(actualResponseBody.getProperties().get("fields")).isNotNull();
      assertThat(actualResponseBody.getProperties().get("messages")).isNotNull();

      String[] actualFields = ((String) actualResponseBody.getProperties().get("fields")).split(";");
      String[] actualMessages = ((String) actualResponseBody.getProperties().get("messages")).split(";");

      assertThat(actualFields).isNotNull().hasSize(2).contains("name", "name");
      assertThat(actualMessages).isNotNull().hasSize(2)
          .contains("name must not be blank", "name must contain between 2 and 150 characters long");
    }

    @Test
    @DisplayName("POST /api/v1/users must return status 400 when passwords do not match")
    void register_MustReturnStatus400_WhenPasswordDoNotMatch() throws Exception {
      String requestBody = """
          {
            "name": "John Doe",
            "email": "john.doe@email.com",
            "password": "1234567890",
            "confirmPassword": "123456789_"
          }
          """;

      mockMvc.perform(post(REGISTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/users must return ProblemDetail when passwords do not match")
    void register_MustReturnProblemDetail_WhenPasswordDoNotMatch() throws Exception {
      String requestBody = """
          {
            "name": "John Doe",
            "email": "john.doe@email.com",
            "password": "1234567890",
            "confirmPassword": "123456789_"
          }
          """;

      String actualContent = mockMvc.perform(post(REGISTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Bad request");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("passwords does not match");
      assertThat(actualResponseBody.getStatus()).isEqualTo(400);
      assertThat(actualResponseBody.getProperties()).isNull();
    }

    @Test
    @DisplayName("POST /api/v1/users must return status 400 when already exists user with given E-mail address")
    void register_MustReturnStatus400_WhenAlreadyExistsUserWithGivenEmailAddress() throws Exception {
      when(userMapperMock.toUser(any())).thenReturn(UserFaker.userToBeRegistered());
      when(authorityServiceMock.findByName("USER")).thenReturn(AuthorityFaker.userAuthority());
      when(passwordEncoderMock.encode("1234567890")).thenReturn("encoded-password");
      when(userRepositoryMock.save(any()))
          .thenThrow(new DataIntegrityViolationException("email field violates the unique constraint"));

      String requestBody = """
          {
            "name": "John Doe",
            "email": "john.doe@email.com",
            "password": "1234567890",
            "confirmPassword": "1234567890"
          }
          """;

      mockMvc.perform(post(REGISTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/users must return ProblemDetail when already exists user with given E-mail address")
    void register_MustReturnProblemDetail_WhenAlreadyExistsUserWithGivenEmailAddress() throws Exception {
      when(userMapperMock.toUser(any())).thenReturn(UserFaker.userToBeRegistered());
      when(authorityServiceMock.findByName("USER")).thenReturn(AuthorityFaker.userAuthority());
      when(passwordEncoderMock.encode("1234567890")).thenReturn("encoded-password");
      when(userRepositoryMock.save(any()))
          .thenThrow(new DataIntegrityViolationException("email field violates the unique constraint"));

      String requestBody = """
          {
            "name": "John Doe",
            "email": "john.doe@email.com",
            "password": "1234567890",
            "confirmPassword": "1234567890"
          }
          """;

      String actualContent = mockMvc.perform(post(REGISTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualResponseBody = mapper.readValue(actualContent, ProblemDetail.class);

      assertThat(actualResponseBody).isNotNull();
      assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Bad request");
      assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("Email {john.doe@email.com} already in use");
      assertThat(actualResponseBody.getStatus()).isEqualTo(400);
    }

  }

  @Nested
  @DisplayName("Tests for fetch endpoint")
  class FetchEndpoint {

    private static final String FETCH_URI = "/api/v1/users";

    @Test
    @DisplayName("GET /api/v1/users must return status 200 when fetch successfully")
    void fetch_MustReturnStatus200_WhenFetchSuccessfully() throws Exception {
      when(userRepositoryMock.findProjectedBy(any())).thenReturn(UserFaker.userSummaryProjectionPage());
      when(userMapperMock.toUserSummaryResponse(any())).thenReturn(UserFaker.userSummaryResponse());

      mockMvc.perform(get(FETCH_URI))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/users must return Page<UserSummaryResponse> when fetch successfully")
    void fetch_MustReturnPageUserSummaryResponse_WhenFetchSuccessfully() throws Exception {
      when(userRepositoryMock.findProjectedBy(any())).thenReturn(UserFaker.userSummaryProjectionPage());
      when(userMapperMock.toUserSummaryResponse(any())).thenReturn(UserFaker.userSummaryResponse());

      String actualContent = mockMvc.perform(get(FETCH_URI))
          .andReturn().getResponse().getContentAsString();

      Page<UserSummaryResponse> actualResponseBody = mapper
          .readValue(actualContent, new TypeReference<PageableResponse<UserSummaryResponse>>() {
          });

      assertThat(actualResponseBody).isNotNull().hasSize(1);
      assertThat(actualResponseBody.getTotalElements()).isEqualTo(1);
      assertThat(actualResponseBody.getNumber()).isZero();
      assertThat(actualResponseBody.getSize()).isEqualTo(20);
    }

    @Test
    @DisplayName("GET /api/v1/users must return status 200 when fetch an page without users")
    void fetch_MustReturnStatus200_WhenFetchAnPageWithoutUsers() throws Exception {
      when(userRepositoryMock.findProjectedBy(any()))
          .thenReturn(new PageImpl<>(List.of(), PageRequest.of(1, 20), 1));

      mockMvc.perform(get(FETCH_URI).param("page", "1"))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/users must return empty Page when fetch an page without users")
    void fetch_MustReturnEmptyPage_WhenFetchAnPageWithoutUsers() throws Exception {
      when(userRepositoryMock.findProjectedBy(any()))
          .thenReturn(new PageImpl<>(List.of(), PageRequest.of(1, 20), 1));

      String actualContent = mockMvc.perform(get(FETCH_URI).param("page", "1"))
          .andReturn().getResponse().getContentAsString();

      Page<UserSummaryResponse> actualResponseBody = mapper
          .readValue(actualContent, new TypeReference<PageableResponse<UserSummaryResponse>>() {
          });

      assertThat(actualResponseBody).isNotNull().isEmpty();
      assertThat(actualResponseBody.getTotalElements()).isEqualTo(1);
      assertThat(actualResponseBody.getNumber()).isOne();
      assertThat(actualResponseBody.getSize()).isEqualTo(20);
    }

  }

}
