package br.com.emendes.yourreviewapi.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Interface service com as abstrações para manipulação de JWT.
 */
public interface JWTService {

  /**
   * Gera um JWT de acordo com os dados em userDetails e o tempo de expiração em mili segundos.
   * @param userDetails objeto com as credenciais do usuário a quem pertencerá o jwt.
   * @param expiration tempo (em mili segundos) de expiração do token.
   * @return String que representa o token.
   */
  String generateToken(UserDetails userDetails, long timeExpiration);

}
