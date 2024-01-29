package br.com.emendes.yourreviewapi.service.impl;

import br.com.emendes.yourreviewapi.dto.request.UserRegisterRequest;
import br.com.emendes.yourreviewapi.dto.response.UserDetailsResponse;
import br.com.emendes.yourreviewapi.exception.EmailAlreadyInUseException;
import br.com.emendes.yourreviewapi.exception.PasswordsDoesNotMatchException;
import br.com.emendes.yourreviewapi.mapper.UserMapper;
import br.com.emendes.yourreviewapi.model.Status;
import br.com.emendes.yourreviewapi.model.entity.User;
import br.com.emendes.yourreviewapi.repository.UserRepository;
import br.com.emendes.yourreviewapi.service.AuthorityService;
import br.com.emendes.yourreviewapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static br.com.emendes.yourreviewapi.util.constants.AuthorityConstants.USER_AUTHORITY;

/**
 * Implementação de {@link UserService}.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final AuthorityService authorityService;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  /**
   * @throws PasswordsDoesNotMatchException caso {@link UserRegisterRequest#password()} e
   *                                        {@link UserRegisterRequest#confirmPassword()} não corresponderem.
   * @throws EmailAlreadyInUseException     caso o email fornecido já esteja em uso no sistema.
   */
  @Override
  public UserDetailsResponse register(UserRegisterRequest userRegisterRequest) {
    log.info("attempt to register user with email: {}", userRegisterRequest.email());
    if (!userRegisterRequest.password().equals(userRegisterRequest.confirmPassword())) {
      throw new PasswordsDoesNotMatchException("passwords does not match");
    }
    User user = userMapper.toUser(userRegisterRequest);

    user.addAuthority(authorityService.findByName(USER_AUTHORITY));
    user.setStatus(Status.ENABLED);
    user.setCreatedAt(LocalDateTime.now());

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    try {
      user = userRepository.save(user);
      log.info("user has been successfully registered with email {}", user.getEmail());

      return userMapper.toUserDetailsResponse(user);
    } catch (DataIntegrityViolationException e) {
      String message = "Email {%s} already in use".formatted(user.getEmail());
      log.info("fail to persist user. {}", message);
      throw new EmailAlreadyInUseException(message);
    }
  }

}
