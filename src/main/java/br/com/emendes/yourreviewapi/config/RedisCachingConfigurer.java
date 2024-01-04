package br.com.emendes.yourreviewapi.config;

import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe com configurações de caching com Redis.
 */
@Configuration
public class RedisCachingConfigurer implements CachingConfigurer {

  @Bean
  @Override
  public CacheErrorHandler errorHandler() {
    return new RedisCachingErrorHandler();
  }

}
