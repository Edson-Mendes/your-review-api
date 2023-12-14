package br.com.emendes.yourreviewapi.repository;

import br.com.emendes.yourreviewapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface repository com as abstrações para interação com o recurso User no banco de dados.
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
