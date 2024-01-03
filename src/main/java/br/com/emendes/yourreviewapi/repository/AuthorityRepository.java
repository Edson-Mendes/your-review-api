package br.com.emendes.yourreviewapi.repository;

import br.com.emendes.yourreviewapi.model.entity.Authority;
import br.com.emendes.yourreviewapi.util.constants.CacheConstants;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interface repository com as abstrações para interação com o recurso Authority no banco de dados.
 */
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

  /**
   * Busca Authority por nome.
   *
   * @param name nome da Authority a ser buscada.
   * @return {@code Optional<Authority>}.
   */
  @Cacheable(value = {CacheConstants.AUTHORITY_CACHE_NAME}, unless = "#result == null")
  Optional<Authority> findByName(String name);

}
