package br.com.emendes.yourreviewapi.config;

import br.com.emendes.yourreviewapi.util.constants.CacheConstants;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Classe com as configurações do cache manager.
 */
@Component
public class CacheCustomizer implements CacheManagerCustomizer<ConcurrentMapCacheManager> {

  @Override
  public void customize(ConcurrentMapCacheManager cacheManager) {
    cacheManager.setCacheNames(Set.of(CacheConstants.USERS_CACHE_NAME, CacheConstants.AUTHORITY_CACHE_NAME));
  }

}
