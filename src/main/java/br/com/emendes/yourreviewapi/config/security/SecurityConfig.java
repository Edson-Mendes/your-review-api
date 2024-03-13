package br.com.emendes.yourreviewapi.config.security;

import br.com.emendes.yourreviewapi.config.security.filter.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe com as configurações de segurança da aplicação.
 */
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

  private final JWTAuthenticationFilter jwtAuthenticationFilter;

  private static final String[] GET_WHITELIST = {"/api/v1/movies", "/api/v1/movies/*"};
  private static final String[] POST_WHITELIST = {"/api/v1/auth/signin", "/api/v1/users"};
  private static final String[] SWAGGER_WHITELIST = {
      "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/favicon.ico"};

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf((AbstractHttpConfigurer::disable));
    http.httpBasic(AbstractHttpConfigurer::disable);

    http.authorizeHttpRequests(
        authorize -> authorize
            .requestMatchers(HttpMethod.GET, GET_WHITELIST).permitAll()
            .requestMatchers(HttpMethod.GET, SWAGGER_WHITELIST).permitAll()
            .requestMatchers(HttpMethod.POST, POST_WHITELIST).permitAll()
            .anyRequest().authenticated()
    );

    http
        .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(exceptionHandlingConfig -> {
          exceptionHandlingConfig.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
          exceptionHandlingConfig.accessDeniedHandler(
              ((request, response, accessDeniedException) -> response.setStatus(HttpStatus.FORBIDDEN.value()))
          );
        });

    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

}
