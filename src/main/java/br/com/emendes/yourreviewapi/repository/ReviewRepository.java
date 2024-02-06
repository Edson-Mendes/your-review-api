package br.com.emendes.yourreviewapi.repository;

import br.com.emendes.yourreviewapi.model.entity.MovieVotes;
import br.com.emendes.yourreviewapi.model.entity.Review;
import br.com.emendes.yourreviewapi.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Interface repository com as abstrações para interação com o recurso Review no banco de dados.
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {

  /**
   * Busca paginada de MovieVotes.
   *
   * @param movieVotes objeto MovieVotes ao qual as reviews deve estar relacionadas.
   * @param pageable   objeto com o modo como deve ser feito a paginação.
   * @return {@code Page<Review>} objeto com a paginação de Review.
   */
  Page<Review> findByMovieVotes(MovieVotes movieVotes, Pageable pageable);

  /**
   * Verifica se existe {@link Review} para o dado {@code user} e {@code movieId}.
   *
   * @param userId  entidade {@link User} que está no relacionamento com {@link Review}.
   * @param movieId identificador do filme.
   * @return {@code true} caso exista uma {@link Review} para o dado {@code user} e {@code movieId}, {@code false caso}
   * contrário.
   */
  @Query("""
      SELECT COUNT(r) > 0
        FROM Review r
        WHERE r.user.id = :userId
        AND r.movieVotes.movieId = :movieId
      """)
  boolean existsByUserIdAndMovieId(@Param("userId") Long userId, @Param("movieId") String movieId);
}
