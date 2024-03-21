package br.com.emendes.yourreviewapi.integration.security;

import br.com.emendes.yourreviewapi.service.JWTService;
import br.com.emendes.yourreviewapi.util.faker.UserFaker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"actuator", "repository-test", "integration", "default-cache"})
@DisplayName("Integration tests for security layer in Authentication endpoints")
class ActuatorSecurityIT {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private JWTService jwtServiceMock;
  @MockBean
  private UserDetailsService userDetailsServiceMock;

  private final String CONTENT_TYPE = "application/json;charset=UTF-8";

  @Nested
  @DisplayName("Integration tests for security in /actuator endpoint")
  class ActuatorEndpoint {

    private final String ACTUATOR_URI = "/actuator";

    @Test
    @DisplayName("/actuator must return status 200 when user has authority ADMIN")
    void actuator_MustReturnStatus200_WhenUserHasAuthorityADMIN() throws Exception {
      when(jwtServiceMock.isTokenValid(any())).thenReturn(true);
      when(jwtServiceMock.extractSubject("thisIsA.mock.AdminJWT")).thenReturn("john.doe@email.com");
      when(userDetailsServiceMock.loadUserByUsername("john.doe@email.com")).thenReturn(UserFaker.userAdmin());

      mockMvc.perform(get(ACTUATOR_URI)
              .contentType(CONTENT_TYPE)
              .header("authorization", "Bearer thisIsA.mock.AdminJWT"))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("/actuator must return status 401 when user is not authenticated")
    void actuator_MustReturnStatus401_WhenUserIsNotAuthenticated() throws Exception {
      mockMvc.perform(get(ACTUATOR_URI)
              .contentType(CONTENT_TYPE))
          .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("/actuator must return status 403 when user has authority USER")
    void actuator_MustReturnStatus403_WhenUserHasAuthorityUSER() throws Exception {
      when(jwtServiceMock.isTokenValid(any())).thenReturn(true);
      when(jwtServiceMock.extractSubject("thisIsA.mock.JWT")).thenReturn("john.doe@email.com");
      when(userDetailsServiceMock.loadUserByUsername("john.doe@email.com")).thenReturn(UserFaker.user());

      mockMvc.perform(get(ACTUATOR_URI)
              .contentType(CONTENT_TYPE)
              .header("authorization", "Bearer thisIsA.mock.JWT"))
          .andExpect(status().isForbidden());
    }

  }

  @Nested
  @DisplayName("Integration tests for security in /actuator/** endpoint")
  class ActuatorSpecificResourceEndpoint {

    private final String ACTUATOR_TEMPLATE_URI = "/actuator/{resource}";

    @ParameterizedTest
    @ValueSource(strings = {"caches", "health", "metrics"})
    @DisplayName("/actuator/** must return status 200 when user has authority ADMIN")
    void actuatorSpecificResource_MustReturnStatus200_WhenUserHasAuthorityADMIN(String resource) throws Exception {
      when(jwtServiceMock.isTokenValid(any())).thenReturn(true);
      when(jwtServiceMock.extractSubject("thisIsA.mock.AdminJWT")).thenReturn("john.doe@email.com");
      when(userDetailsServiceMock.loadUserByUsername("john.doe@email.com")).thenReturn(UserFaker.userAdmin());

      mockMvc.perform(get(ACTUATOR_TEMPLATE_URI, resource)
              .contentType(CONTENT_TYPE)
              .header("authorization", "Bearer thisIsA.mock.AdminJWT"))
          .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"caches", "health", "metrics"})
    @DisplayName("/actuator/** must return status 401 when user is not authenticated")
    void actuatorSpecificResource_MustReturnStatus401_WhenUserIsNotAuthenticated(String resource) throws Exception {
      mockMvc.perform(get(ACTUATOR_TEMPLATE_URI, resource)
              .contentType(CONTENT_TYPE)
              .header("authorization", "Bearer thisIsA.mock.AdminJWT"))
          .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"caches", "health", "metrics"})
    @DisplayName("/actuator/** must return status 403 when user has authority USER")
    void actuatorSpecificResource_MustReturnStatus403_WhenUserHasAuthorityUSER(String resource) throws Exception {
      when(jwtServiceMock.isTokenValid(any())).thenReturn(true);
      when(jwtServiceMock.extractSubject("thisIsA.mock.AdminJWT")).thenReturn("john.doe@email.com");
      when(userDetailsServiceMock.loadUserByUsername("john.doe@email.com")).thenReturn(UserFaker.user());

      mockMvc.perform(get(ACTUATOR_TEMPLATE_URI, resource)
              .contentType(CONTENT_TYPE)
              .header("authorization", "Bearer thisIsA.mock.AdminJWT"))
          .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"info", "conditions", "beans", "configprops", "env", "flyway",
        "loggers", "heapdump", "threaddump", "scheduledtasks", "mappings"})
    @DisplayName("/actuator/** must return status 404 when resource is not exposed")
    void actuatorSpecificResource_MustReturnStatus404_WhenResourceIsNotExposed(String resourceNonExposed) throws Exception {
      when(jwtServiceMock.isTokenValid(any())).thenReturn(true);
      when(jwtServiceMock.extractSubject("thisIsA.mock.AdminJWT")).thenReturn("john.doe@email.com");
      when(userDetailsServiceMock.loadUserByUsername("john.doe@email.com")).thenReturn(UserFaker.userAdmin());

      mockMvc.perform(get(ACTUATOR_TEMPLATE_URI, resourceNonExposed)
              .contentType(CONTENT_TYPE)
              .header("authorization", "Bearer thisIsA.mock.AdminJWT"))
          .andExpect(status().isNotFound());
    }

  }

}
