package br.com.emendes.yourreviewapi.config.security.filter;

import br.com.emendes.yourreviewapi.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static org.apache.tomcat.websocket.Constants.AUTHORIZATION_HEADER_NAME;

/**
 * Filtro responsável por autenticar requisição via JWT.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

  private final JWTService jwtService;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {
    Optional<String> authorizationHeaderOptional = extractHeader(request, AUTHORIZATION_HEADER_NAME);

    if (authorizationHeaderOptional.isEmpty()) {
      log.info("empty authorization header");
      filterChain.doFilter(request, response);
      return;
    }

    String authorizationHeaderValue = authorizationHeaderOptional.get();
    if (!authorizationHeaderValue.startsWith("Bearer ")) {
      log.info("invalid format for authorization header");
      filterChain.doFilter(request, response);
      return;
    }
    String token = extractToken(authorizationHeaderValue);

    if (jwtService.isTokenValid(token)) {
      String username = jwtService.extractSubject(token);
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      Authentication authenticationToken = generateAuthenticationToken(userDetails);

      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    filterChain.doFilter(request, response);
  }

  /**
   * Extrai o token de uma string no formado 'Bearer token_value'.
   *
   * @param authenticationHeader string que representa o header authorization.
   * @return String que representa o token.
   */
  private String extractToken(String authenticationHeader) {
    return authenticationHeader.substring(7);
  }

  /**
   * Extrai o header da requisição.
   *
   * @param request    objeto que representa a requisição.
   * @param headerName nome do header que deve ser extraído.
   * @return {@code Optional<String>} Contendo ou não o header desejado.
   */
  private Optional<String> extractHeader(HttpServletRequest request, String headerName) {
    return Optional.ofNullable(request.getHeader(headerName));
  }

  /**
   * Gera Authentication a partir de um UserDetails.
   *
   * @param userDetails UserDetails com as informações de autenticação do usuário.
   * @return Authentication que representa o usuário autenticado.
   */
  private static Authentication generateAuthenticationToken(UserDetails userDetails) {
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

}
