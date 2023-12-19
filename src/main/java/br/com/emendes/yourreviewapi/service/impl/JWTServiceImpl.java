package br.com.emendes.yourreviewapi.service.impl;

import br.com.emendes.yourreviewapi.service.JWTService;
import br.com.emendes.yourreviewapi.util.properties.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * Implementação de {@link JWTService}.
 */
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

  /**
   * Gera uma Key com base na secret.
   */
  private Key getKey() {
    byte[] secretBytes = jwtProperties.secret().getBytes();
    return Keys.hmacShaKeyFor(secretBytes);
  }

}
