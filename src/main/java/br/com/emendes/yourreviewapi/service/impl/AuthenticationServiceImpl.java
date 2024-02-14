package br.com.emendes.yourreviewapi.service.impl;

import br.com.emendes.yourreviewapi.dto.request.AuthenticationRequest;
import br.com.emendes.yourreviewapi.dto.response.AuthenticationResponse;
import br.com.emendes.yourreviewapi.model.entity.User;
import br.com.emendes.yourreviewapi.service.AuthenticationService;
import br.com.emendes.yourreviewapi.service.JWTService;
import br.com.emendes.yourreviewapi.util.component.AuthenticatedUserComponent;
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
  private final AuthenticatedUserComponent authenticatedUserComponent;
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

  @Override
  public AuthenticationResponse refreshToken() {
    User currentUser = authenticatedUserComponent.getCurrentUser();
    log.info("attempt to refresh token for user {}", currentUser.getUsername());

    String token = jwtService.generateToken(currentUser, authenticationTokenTime);
    log.info("token generate successfully for user : {}", currentUser.getUsername());

    return AuthenticationResponse.builder()
        .type("Bearer")
        .token(token)
        .build();
  }

}
