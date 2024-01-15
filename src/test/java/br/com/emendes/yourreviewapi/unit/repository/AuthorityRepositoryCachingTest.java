package br.com.emendes.yourreviewapi.unit.repository;

import br.com.emendes.yourreviewapi.repository.AuthorityRepository;
import br.com.emendes.yourreviewapi.util.constants.CacheConstants;
import br.com.emendes.yourreviewapi.util.faker.AuthorityFaker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AopTestUtils;

import java.util.Optional;

@ContextConfiguration
@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for Caching data from AuthorityRepository")
class AuthorityRepositoryCachingTest {

  private AuthorityRepository mock;
  @Autowired
  private AuthorityRepository authorityRepository;

  @BeforeEach
  void setUp() {
    mock = AopTestUtils.getTargetObject(authorityRepository);
    BDDMockito.reset(mock);

    BDDMockito.when(mock.findByName("USER"))
        .thenReturn(Optional.of(AuthorityFaker.userAuthority()));
  }

  @Test
  @DisplayName("findByName must use cached result when call AuthorityRepository#findByName three times")
  void findByName_MustUseCachedResult_WhenCallAuthorityRepositoryThreeTimes() {
    Assertions.assertThat(authorityRepository.findByName("USER")).isNotNull().isEqualTo(Optional.of(AuthorityFaker.userAuthority()));
    Assertions.assertThat(authorityRepository.findByName("USER")).isNotNull().isEqualTo(Optional.of(AuthorityFaker.userAuthority()));
    Assertions.assertThat(authorityRepository.findByName("USER")).isNotNull().isEqualTo(Optional.of(AuthorityFaker.userAuthority()));

    BDDMockito.verify(mock).findByName("USER");
  }

  @EnableCaching
  @Configuration
  public static class CachingTestConfig {

    @Bean
    public AuthorityRepository authorityRepositoryMock() {
      return BDDMockito.mock(AuthorityRepository.class);
    }

    @Bean
    public CacheManager cacheManager() {
      return new ConcurrentMapCacheManager(CacheConstants.AUTHORITY_CACHE_NAME);
    }

  }

}
