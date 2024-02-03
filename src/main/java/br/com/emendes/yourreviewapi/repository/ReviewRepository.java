package br.com.emendes.yourreviewapi.repository;

import br.com.emendes.yourreviewapi.model.entity.MovieVotes;
import br.com.emendes.yourreviewapi.model.entity.Review;
import br.com.emendes.yourreviewapi.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface repository com as abstrações para interação com o recurso Review no banco de dados.
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {

  /**
   * Verifica se existe {@link Review} para o dado {@link User} e {@link MovieVotes}.
   *
   * @param user       usuário a ser verificado se já fez uma avaliação para o filme em questão.
   * @param movieVotes MovieVotes com o id do filme a ser verificado se já recebeu uma avaliação do usuário.
   * @return {@code true} caso já exista uma avaliação do usuário para o dado filme, {@code false} caso contrário.
   */
  boolean existsByUserAndMovieVotes(User user, MovieVotes movieVotes);

  /**
   * Busca paginada de MovieVotes.
   *
   * @param movieVotes objeto MovieVotes ao qual as reviews deve estar relacionadas.
   * @param pageable   objeto com o modo como deve ser feito a paginação.
   * @return {@code Page<Review>} objeto com a paginação de Review.
   */
  Page<Review> findByMovieVotes(MovieVotes movieVotes, Pageable pageable);

}
