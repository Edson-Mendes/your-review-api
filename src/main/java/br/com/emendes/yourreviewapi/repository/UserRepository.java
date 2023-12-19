package br.com.emendes.yourreviewapi.repository;

import br.com.emendes.yourreviewapi.model.entity.User;
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
  Optional<User> findByEmail(String email);

}
