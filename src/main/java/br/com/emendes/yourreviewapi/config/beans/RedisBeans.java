package br.com.emendes.yourreviewapi.config.beans;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;

import static br.com.emendes.yourreviewapi.util.constants.CacheConstants.AUTHORITY_CACHE_NAME;
import static br.com.emendes.yourreviewapi.util.constants.CacheConstants.USERS_CACHE_NAME;

/**
 * Classe com as configurações das caches do sistema.
 */
@Configuration
@Profile({"redis"})
public class RedisBeans {

  @Bean
  public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
    return builder -> builder
        .withCacheConfiguration(USERS_CACHE_NAME,
            RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues()
        )
        .withCacheConfiguration(AUTHORITY_CACHE_NAME,
            RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(24))
                .disableCachingNullValues()
        );
  }

}
