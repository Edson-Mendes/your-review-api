package br.com.emendes.yourreviewapi.service.impl;

import br.com.emendes.yourreviewapi.exception.AuthorityNotFoundException;
import br.com.emendes.yourreviewapi.model.entity.Authority;
import br.com.emendes.yourreviewapi.repository.AuthorityRepository;
import br.com.emendes.yourreviewapi.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Implementação de {@link AuthorityService}.
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class AuthorityServiceImpl implements AuthorityService {

  private final AuthorityRepository authorityRepository;

  @Override
  public Authority findByName(String name) {
    log.info("searching for authority with name: {}", name);
    Assert.notNull(name, "name must not be null");

    return authorityRepository.findByName(name)
        .orElseThrow(() -> new AuthorityNotFoundException("Authority not found for name: %s".formatted(name)));
  }

}
