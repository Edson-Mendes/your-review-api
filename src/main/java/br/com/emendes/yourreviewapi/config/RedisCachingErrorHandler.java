package br.com.emendes.yourreviewapi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

/**
 * Classe error handler para uso em caso de cache com Redis.<br><br>
 * <p>
 * Essa classe apenas mostrará logs do erro que ocorrer, o motivo disso é,
 * caso um erro ocorra com o Redis (e.g. Server do Redis estar off) então o sistema continuará o seu fluxo
 * buscando os dados diretamente no datasource.
 */
@Slf4j
public class RedisCachingErrorHandler implements CacheErrorHandler {

  public static final String EXCEPTION_MESSAGE_TEMPLATE = "Exception message: {}";

  @Override
  public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
    log.error("An error occurred obtaining data from cache: [{}], with key: [{}]", cache.getName(), key);
    log.error(EXCEPTION_MESSAGE_TEMPLATE, exception.getMessage());
  }

  @Override
  public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
    log.error("An error occurred putting data from cache: [{}], with key: [{}]", cache.getName(), key);
    log.error(EXCEPTION_MESSAGE_TEMPLATE, exception.getMessage());
  }

  @Override
  public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
    log.error("An error occurred evicting data from cache: [{}], with key: [{}]", cache.getName(), key);
    log.error(EXCEPTION_MESSAGE_TEMPLATE, exception.getMessage());
  }

  @Override
  public void handleCacheClearError(RuntimeException exception, Cache cache) {
    log.error("An error occurred clearing cache: [{}]", cache.getName());
    log.error(EXCEPTION_MESSAGE_TEMPLATE, exception.getMessage());
  }

}
