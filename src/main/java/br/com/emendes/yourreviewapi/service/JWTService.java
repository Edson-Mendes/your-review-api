package br.com.emendes.yourreviewapi.service;

import io.jsonwebtoken.JwtException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Interface service com as abstrações para manipulação de JWT.
 */
public interface JWTService {

  /**
   * Gera um JWT de acordo com os dados em userDetails e o tempo de expiração em mili segundos.
   *
   * @param userDetails    objeto com as credenciais do usuário a quem pertencerá o jwt.
   * @param timeExpiration tempo (em mili segundos) de expiração do token.
   * @return String que representa o token.
   */
  String generateToken(UserDetails userDetails, long timeExpiration);

  /**
   * Verifica se o token informado é válido.
   *
   * @param token objeto a ser válidado.
   * @return true caso o token seja válido, false caso contrário.
   */
  boolean isTokenValid(String token);

  /**
   * Extrai o subject do token.
   *
   * @param token objeto o qual o subject será estraído.
   * @return o subject do JWT.
   * @throws JwtException caso o token sejá inválido.
   */
  String extractSubject(String token);

}
