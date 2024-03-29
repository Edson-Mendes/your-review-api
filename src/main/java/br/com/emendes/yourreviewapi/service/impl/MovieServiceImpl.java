package br.com.emendes.yourreviewapi.service.impl;

import br.com.emendes.yourreviewapi.client.MovieClient;
import br.com.emendes.yourreviewapi.dto.response.MovieDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.MovieSummaryResponse;
import br.com.emendes.yourreviewapi.mapper.MovieMapper;
import br.com.emendes.yourreviewapi.service.MovieService;
import br.com.emendes.yourreviewapi.service.MovieVotesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Implementação de {@link MovieService}.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {

  private final MovieClient movieClient;
  private final MovieMapper movieMapper;
  private final MovieVotesService movieVotesService;

  @Override
  public Page<MovieSummaryResponse> findByName(String name, int page) {
    log.info("Searching for movies with name: {} and page: {}", name, page);

    return movieClient.findByName(name, page).map(movieMapper::toMovieSummaryResponse);
  }

  @Override
  public MovieDetailsResponse findDetailedById(String movieId) {
    log.info("Searching for movie with id: {}", movieId);

    return movieMapper.toMovieDetailsResponse(
        movieClient.findById(movieId),
        movieVotesService.findAverageByMovieId(movieId).orElse(null));
  }

  @Override
  public MovieSummaryResponse findSummarizedById(String movieId) {
    log.info("Searching for movie with id: {}", movieId);

    return movieMapper.toMovieSummaryResponse(movieClient.findById(movieId));
  }

}
