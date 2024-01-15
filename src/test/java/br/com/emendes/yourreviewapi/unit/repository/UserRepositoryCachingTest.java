package br.com.emendes.yourreviewapi.unit.repository;

import br.com.emendes.yourreviewapi.repository.UserRepository;
import br.com.emendes.yourreviewapi.util.constants.CacheConstants;
import br.com.emendes.yourreviewapi.util.faker.UserFaker;
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
@DisplayName("Unit tests for Caching data from UserRepository")
class UserRepositoryCachingTest {

  private UserRepository mock;
  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    mock = AopTestUtils.getTargetObject(userRepository);
    BDDMockito.reset(mock);

    BDDMockito.when(mock.findByEmail("lorem@email.com"))
        .thenReturn(Optional.of(UserFaker.user()));
  }

  @Test
  @DisplayName("findByEmail must use cached result when call UserRepository#findByEmail three times")
  void findByEmail_MustUseCachedResult_WhenCallUserRepositoryThreeTimes() {
    Assertions.assertThat(userRepository.findByEmail("lorem@email.com")).isNotNull().isEqualTo(Optional.of(UserFaker.user()));
    Assertions.assertThat(userRepository.findByEmail("lorem@email.com")).isNotNull().isEqualTo(Optional.of(UserFaker.user()));
    Assertions.assertThat(userRepository.findByEmail("lorem@email.com")).isNotNull().isEqualTo(Optional.of(UserFaker.user()));

    BDDMockito.verify(mock).findByEmail("lorem@email.com");
  }

  @EnableCaching
  @Configuration
  public static class CachingTestConfig {

    @Bean
    public UserRepository userRepositoryMock() {
      return BDDMockito.mock(UserRepository.class);
    }

    @Bean
    public CacheManager cacheManager() {
      return new ConcurrentMapCacheManager(CacheConstants.USERS_CACHE_NAME);
    }

  }

}
