package br.com.emendes.yourreviewapi.config;

import br.com.emendes.yourreviewapi.util.constants.CacheConstants;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Classe com as configurações de cache manager padrão, ou seja, caso não seja usado um banco de dados para cache
 * será usaado um cache padrão em memória.
 */
@Component
@Profile({"default-cache"})
public class DefaultCacheCustomizer implements CacheManagerCustomizer<ConcurrentMapCacheManager> {

  @Override
  public void customize(ConcurrentMapCacheManager cacheManager) {
    cacheManager.setCacheNames(Set.of(CacheConstants.USERS_CACHE_NAME, CacheConstants.AUTHORITY_CACHE_NAME));
  }

}
