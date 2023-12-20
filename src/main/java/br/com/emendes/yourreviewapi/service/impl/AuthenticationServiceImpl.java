package br.com.emendes.yourreviewapi.service.impl;

import br.com.emendes.yourreviewapi.dto.request.AuthenticationRequest;
import br.com.emendes.yourreviewapi.dto.response.AuthenticationResponse;
import br.com.emendes.yourreviewapi.service.AuthenticationService;
import br.com.emendes.yourreviewapi.service.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Implementação de {@link AuthenticationService}.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final JWTService jwtService;
  @Value("${your-review-api.jwt.expiration.authentication}")
  private long authenticationTokenTime;

  @Override
  public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(
            authenticationRequest.username(), authenticationRequest.password()));

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    String token = jwtService.generateToken(userDetails, authenticationTokenTime);
    log.info("token generate successfully for user : {}", userDetails.getUsername());

    return AuthenticationResponse.builder()
        .type("Bearer")
        .token(token)
        .build();
  }

}
