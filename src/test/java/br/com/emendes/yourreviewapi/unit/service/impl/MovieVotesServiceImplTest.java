package br.com.emendes.yourreviewapi.unit.service.impl;

import br.com.emendes.yourreviewapi.client.MovieClient;
import br.com.emendes.yourreviewapi.dto.MovieVotesAverage;
import br.com.emendes.yourreviewapi.exception.MovieNotFoundException;
import br.com.emendes.yourreviewapi.model.entity.MovieVotes;
import br.com.emendes.yourreviewapi.repository.MovieVotesRepository;
import br.com.emendes.yourreviewapi.service.impl.MovieVotesServiceImpl;
import br.com.emendes.yourreviewapi.util.faker.MovieFaker;
import br.com.emendes.yourreviewapi.util.faker.MovieVotesFaker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests de MovieVotesServiceImpl")
class MovieVotesServiceImplTest {

  @InjectMocks
  private MovieVotesServiceImpl movieVotesService;
  @Mock
  private MovieVotesRepository movieVotesRepositoryMock;
  @Mock
  private MovieClient movieClientMock;

  @Nested
  @DisplayName("FindByMovieId Method")
  class FindByMovieIdMethod {

    @Test
    @DisplayName("findByMovieId must return Optional<MovieVotes> when found successfully")
    void findByMovieId_MustReturnOptionalWithMovieVotes_WhenFoundSuccessfully() {
      when(movieVotesRepositoryMock.findByMovieId("1234")).thenReturn(MovieVotesFaker.movieVotesOptional());

      Optional<MovieVotes> actualMovieVotesOptional = movieVotesService.findByMovieId("1234");

      assertThat(actualMovieVotesOptional).isNotNull().isPresent();
      MovieVotes actualMovieVotes = actualMovieVotesOptional.orElseThrow();
      assertThat(actualMovieVotes).isNotNull();
      assertThat(actualMovieVotes.getMovieId()).isNotNull().isEqualTo("1234");
    }

    @Test
    @DisplayName("findByMovieId must return empty Optional when not found MovieVotes")
    void findByMovieId_MustReturnEmptyOptional_WhenNotFoundMovieVotes() {
      when(movieVotesRepositoryMock.findByMovieId("1234")).thenReturn(Optional.empty());

      Optional<MovieVotes> actualMovieVotesOptional = movieVotesService.findByMovieId("1234");

      assertThat(actualMovieVotesOptional).isNotNull().isNotPresent();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("findByMovieId must throw IllegalArgumentException when movieId is invalid")
    void findByMovieId_MustThrowIllegalArgumentException_WhenMovieIdIsInvalid(String invalidMovieId) {
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> movieVotesService.findByMovieId(invalidMovieId))
          .withMessage("movieId must not be null, empty or blank");
    }

  }

  @Nested
  @DisplayName("GenerateNonVotedMovieVotes Method")
  class GenerateNonVotedMovieVotesMethod {

    @Test
    @DisplayName("generateNonVotedMovieVotes must return MovieVotes when generate successfully")
    void generateNonVotedMovieVotes_MustReturnMovieVotes_WhenGenerateSuccessfully() {
      when(movieClientMock.findById("1234")).thenReturn(MovieFaker.movie());

      MovieVotes actualMovieVotes = movieVotesService.generateNonVotedMovieVotes("1234");

      assertThat(actualMovieVotes).isNotNull();
      assertThat(actualMovieVotes.getId()).isNull();
      assertThat(actualMovieVotes.getMovieId()).isNotNull().isEqualTo("1234");
      assertThat(actualMovieVotes.getCreatedAt()).isNotNull();
      assertThat(actualMovieVotes.getVoteCount()).isZero();
      assertThat(actualMovieVotes.getVoteTotal()).isZero();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("generateNonVotedMovieVotes must throw IllegalArgumentException when movieId is invalid")
    void generateNonVotedMovieVotes_MustThrowIllegalArgumentException_WhenMovieIdIsInvalid(String invalidMovieId) {
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> movieVotesService.generateNonVotedMovieVotes(invalidMovieId))
          .withMessage("movieId must not be null, empty or blank");
    }

    @Test
    @DisplayName("generateNonVotedMovieVotes must throw MovieNotFoundException when there is no Movie with given movieId")
    void generateNonVotedMovieVotes_MustThrowMovieNotFoundException_WhenThereIsNoMovieWithGivenMovieId() {
      when(movieClientMock.findById("1234")).thenThrow(new MovieNotFoundException("movie not found with id: 1234"));

      assertThatExceptionOfType(MovieNotFoundException.class)
          .isThrownBy(() -> movieVotesService.generateNonVotedMovieVotes("1234"))
          .withMessage("movie not found with id: 1234");
    }

  }

  @Nested
  @DisplayName("FindAverageByMovieId Method")
  class FindAverageByMovieIdMethod {

    @Test
    @DisplayName("findAverageByMovieId must return Optional<MovieVotesAverage> when found successfully")
    void findAverageByMovieId_MustReturnOptionalWithMovieVotesAverage_WhenFoundSuccessfully() {
      when(movieVotesRepositoryMock.findByMovieId("1234")).thenReturn(MovieVotesFaker.movieVotesOptional());

      Optional<MovieVotesAverage> actualMovieVotesOptional = movieVotesService.findAverageByMovieId("1234");

      assertThat(actualMovieVotesOptional).isNotNull().isPresent();
      MovieVotesAverage actualMovieVotesAverage = actualMovieVotesOptional.orElseThrow();
      assertThat(actualMovieVotesAverage).isNotNull();
      assertThat(actualMovieVotesAverage.id()).isNotNull().isEqualTo(4_321L);
      assertThat(actualMovieVotesAverage.movieId()).isNotNull().isEqualTo("1234");
      assertThat(actualMovieVotesAverage.reviewTotal()).isEqualTo(40);
      assertThat(actualMovieVotesAverage.reviewAverage()).isNotNull().isEqualTo("3.07");
    }

    @Test
    @DisplayName("findAverageByMovieId must return empty Optional when not found MovieVotes")
    void findAverageByMovieId_MustReturnEmptyOptional_WhenNotFoundMovieVotes() {
      when(movieVotesRepositoryMock.findByMovieId("1234")).thenReturn(Optional.empty());

      Optional<MovieVotesAverage> actualMovieVotesOptional = movieVotesService.findAverageByMovieId("1234");

      assertThat(actualMovieVotesOptional).isNotNull().isNotPresent();
    }

    @Test
    @DisplayName("findAverageByMovieId must return empty Optional when MovieVotes founded has zero voteCount")
    void findAverageByMovieId_MustReturnEmptyOptional_WhenMovieVotesFoundedHasZeroVoteCount() {
      when(movieVotesRepositoryMock.findByMovieId("1234"))
          .thenReturn(MovieVotesFaker.movieVotesWithZeroVotesOptional());

      Optional<MovieVotesAverage> actualMovieVotesOptional = movieVotesService.findAverageByMovieId("1234");

      assertThat(actualMovieVotesOptional).isNotNull().isNotPresent();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("findAverageByMovieId must throw IllegalArgumentException when movieId is invalid")
    void findAverageByMovieId_MustThrowIllegalArgumentException_WhenMovieIdIsInvalid(String invalidMovieId) {
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> movieVotesService.findAverageByMovieId(invalidMovieId))
          .withMessage("movieId must not be null, empty or blank");
    }

  }

}