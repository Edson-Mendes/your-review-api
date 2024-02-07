package br.com.emendes.yourreviewapi.service.impl;

import br.com.emendes.yourreviewapi.client.MovieClient;
import br.com.emendes.yourreviewapi.exception.MovieNotFoundException;
import br.com.emendes.yourreviewapi.model.entity.MovieVotes;
import br.com.emendes.yourreviewapi.repository.MovieVotesRepository;
import br.com.emendes.yourreviewapi.service.MovieVotesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Implementação de {@link MovieVotesService}.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MovieVotesServiceImpl implements MovieVotesService {

  private final MovieVotesRepository movieVotesRepository;
  private final MovieClient movieClient;

  @Override
  public Optional<MovieVotes> findByMovieId(String movieId) {
    log.info("Attempt to find MovieVotes with movieId: {}", movieId);
    checkMovieId(movieId);

    return movieVotesRepository.findByMovieId(movieId);
  }

  @Override
  public MovieVotes generateNonVotedMovieVotes(String movieId) {
    log.info("Attempt to generate non voted MovieVotes with movieId: {}", movieId);
    checkMovieId(movieId);

    try {
      movieClient.findById(movieId);

      return MovieVotes.builder()
          .movieId(movieId)
          .voteCount(0)
          .voteTotal(0)
          .createdAt(LocalDateTime.now())
          .build();
    } catch (MovieNotFoundException exception) {
      throw new MovieNotFoundException(exception.getMessage(), 400);
    }
  }

  /**
   * Verifica se movieId não é null ou em branco.
   *
   * @param movieId identificador de Movie a ser verificado.
   * @throws IllegalArgumentException caso {@code movieId} seja null ou blank.
   */
  private static void checkMovieId(String movieId) {
    if (movieId == null || movieId.isBlank()) {
      String errorMessage = "movieId must not be null, empty or blank";
      log.info(errorMessage);
      throw new IllegalArgumentException(errorMessage);
    }
  }

}
