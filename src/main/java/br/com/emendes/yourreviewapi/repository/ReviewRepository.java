package br.com.emendes.yourreviewapi.repository;

import br.com.emendes.yourreviewapi.model.entity.Review;
import br.com.emendes.yourreviewapi.model.entity.User;
import br.com.emendes.yourreviewapi.repository.projection.ReviewDetailsProjection;
import br.com.emendes.yourreviewapi.repository.projection.ReviewSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Interface repository com as abstrações para interação com o recurso Review no banco de dados.
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {

  /**
   * Busca paginada de {@link Review} por {@code movieId}.<br>
   * Os campos buscados são de acordo com a projection {@link ReviewSummaryProjection}.
   *
   * @param movieId  identificador do Movie relacionado com a Review.
   * @param pageable objeto com o modo como deve ser feito a paginação.
   * @return {@code Page<ReviewSummaryProjection>} objeto com a paginação de ReviewSummaryProjection.
   */
  Page<ReviewSummaryProjection> findProjectedByMovieVotesMovieId(String movieId, Pageable pageable);

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

  /**
   * Busca campos {@link Review} de acordo com a projection {@link ReviewDetailsProjection} por identificador da Review.
   *
   * @param reviewId identificador da {@link Review}.
   * @return {@code Optional<ReviewDetailsProjection>} com dados detalhados da Review caso exista,
   * e {@link Optional#empty} caso não exista review para o dado reviewId.
   */
  Optional<ReviewDetailsProjection> findProjectedById(Long reviewId);

  /**
   * Busca {@link Review} por reviewId e user.
   *
   * @param reviewId identificador da Review.
   * @param user     user relacionado com a review de id reviewId.
   * @return {@code Optional<Review>}
   */
  Optional<Review> findByIdAndUser(Long reviewId, User user);

}
