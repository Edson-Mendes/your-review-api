package br.com.emendes.yourreviewapi.service.impl;

import br.com.emendes.yourreviewapi.client.MovieClient;
import br.com.emendes.yourreviewapi.exception.MovieNotFoundException;
import br.com.emendes.yourreviewapi.exception.MovieVotesNotFoundException;
import br.com.emendes.yourreviewapi.model.entity.MovieVotes;
import br.com.emendes.yourreviewapi.repository.MovieVotesRepository;
import br.com.emendes.yourreviewapi.service.MovieVotesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    if (movieId == null || movieId.isBlank()) {
      String errorMessage = "movieId must not be null, empty or blank";
      log.info(errorMessage);
      throw new IllegalArgumentException(errorMessage);
    }

    return movieVotesRepository.findByMovieId(movieId);
  }

  @Override
  public void updateById(Long movieVotesId, int vote) {
    if (vote < 1 || vote > 10) {
      throw new IllegalArgumentException("vote must be less than 1 and greater than 10");
    }

    MovieVotes movieVotes = movieVotesRepository.findById(movieVotesId)
        .orElseThrow(() -> {
          String errorMessage = "MovieVotes not found for id: %s".formatted(movieVotesId);
          log.info(errorMessage);

          return new MovieVotesNotFoundException(errorMessage);
        });

    movieVotes.setVoteCount(movieVotes.getVoteCount() + 1);
    movieVotes.setVoteTotal(movieVotes.getVoteTotal() + vote);
    movieVotesRepository.save(movieVotes);
  }

  @Override
  public MovieVotes register(String movieId) {
    try {
      movieClient.findById(movieId);
      MovieVotes movieVotes = MovieVotes.builder()
          .movieId(movieId)
          .voteTotal(0)
          .voteCount(0)
          .build();

      movieVotes = movieVotesRepository.save(movieVotes);
      log.info("MovieVotes registered successfully with id: {}", movieVotes.getMovieId());
      return movieVotes;
    } catch (MovieNotFoundException e) {
      log.info("Movie not found with id: {}", movieId);
      throw new MovieNotFoundException(e.getMessage(), 400);
    }
  }

}
