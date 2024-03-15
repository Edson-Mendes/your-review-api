package br.com.emendes.yourreviewapi.repository;

import br.com.emendes.yourreviewapi.model.entity.User;
import br.com.emendes.yourreviewapi.repository.projection.UserSummaryProjection;
import br.com.emendes.yourreviewapi.util.constants.CacheConstants;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interface repository com as abstrações para interação com o recurso User no banco de dados.
 */
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Busca usuário por email.
   *
   * @param email E-mail do usuário a ser buscado.
   * @return {@code Optional<User>} objeto wrapper de User.
   */
  @Cacheable(value = {CacheConstants.USERS_CACHE_NAME}, unless = "#result == null")
  Optional<User> findByEmail(String email);

  /**
   * Busca páginada de usuários, busca apenas os campos presentes em {@link UserSummaryProjection}.
   *
   * @param pageable modo como a busca será paginada.
   * @return {@code Page<UserSummaryProjetion>} contendo paginação de UserSummaryProjection.
   */
  Page<UserSummaryProjection> findProjectedBy(Pageable pageable);

}
