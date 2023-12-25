package br.com.emendes.yourreviewapi.service.impl;

import br.com.emendes.yourreviewapi.service.JWTService;
import br.com.emendes.yourreviewapi.util.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * Implementação de {@link JWTService}.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class JWTServiceImpl implements JWTService {

  private final JwtProperties jwtProperties;

  @Override
  public String generateToken(UserDetails userDetails, long timeExpiration) {
    long currentTimeMillis = System.currentTimeMillis();
    Date now = new Date(currentTimeMillis);
    Date expiration = new Date(currentTimeMillis + timeExpiration);

    return Jwts.builder()
        .setIssuer("Your Review API")
        .setSubject(userDetails.getUsername())
        .setIssuedAt(now)
        .setExpiration(expiration)
        .signWith(getKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  @Override
  public boolean isTokenValid(String token) {
    try {
      extractAllClaims(token);
      return true;
    } catch (JwtException exception) {
      log.info("invalid token, error message: {}", exception.getMessage());
      return false;
    }
  }

  @Override
  public String extractSubject(String token) {
    return extractAllClaims(token).getSubject();
  }

  /**
   * Gera uma Key com base na secret.
   */
  private Key getKey() {
    byte[] secretBytes = jwtProperties.secret().getBytes();
    return Keys.hmacShaKeyFor(secretBytes);
  }

  /**
   * Extrai todas as Claims do token.
   *
   * @throws JwtException caso o token sejá inválido (null, em branco, expirado, mal formado).
   */
  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

}
