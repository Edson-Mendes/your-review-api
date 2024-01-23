package br.com.emendes.yourreviewapi.repository;

import br.com.emendes.yourreviewapi.model.entity.MovieVotes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interface repository com as abstrações para interação com o recurso MovieVotes no banco de dados.
 */
public interface MovieVotesRepository extends JpaRepository<MovieVotes, Long> {

  /**
   * Busca MovieVotes por movieId.
   *
   * @param movieId identificador do Movie associado a MovieVotes.
   * @return {@code Optional<MovieVotes>} contendo o MovieVotes encontrado, ou Optional.empty caso contrário.
   */
  Optional<MovieVotes> findByMovieId(String movieId);

}
