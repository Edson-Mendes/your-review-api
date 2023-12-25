package br.com.emendes.yourreviewapi.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

/**
 * Classe com as configurações de segurança da aplicação.
 */
@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf((AbstractHttpConfigurer::disable));
    http.httpBasic(AbstractHttpConfigurer::disable);

    http.authorizeHttpRequests(authorize -> authorize
        .anyRequest().permitAll());

    http
        .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(exceptionHandlingConfig -> {
          exceptionHandlingConfig.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
          exceptionHandlingConfig.accessDeniedHandler(
              ((request, response, accessDeniedException) -> response.setStatus(HttpStatus.FORBIDDEN.value()))
          );
        });

    return http.build();
  }

}
