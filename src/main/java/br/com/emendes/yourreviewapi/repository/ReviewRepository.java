package br.com.emendes.yourreviewapi.repository;

import br.com.emendes.yourreviewapi.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface repository com as abstrações para interação com o recurso Review no banco de dados.
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
