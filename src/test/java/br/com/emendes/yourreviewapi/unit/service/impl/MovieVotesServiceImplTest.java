package br.com.emendes.yourreviewapi.unit.service.impl;

import br.com.emendes.yourreviewapi.client.MovieClient;
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
      when(movieVotesRepositoryMock.findByMovieId("1000000")).thenReturn(MovieVotesFaker.movieVotesOptional());

      Optional<MovieVotes> actualMovieVotesOptional = movieVotesService.findByMovieId("1000000");

      assertThat(actualMovieVotesOptional).isNotNull().isPresent();
      MovieVotes actualMovieVotes = actualMovieVotesOptional.get();
      assertThat(actualMovieVotes).isNotNull();
      assertThat(actualMovieVotes.getMovieId()).isNotNull().isEqualTo("1000000");
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
      when(movieClientMock.findById("1000000")).thenReturn(MovieFaker.movie());

      MovieVotes actualMovieVotes = movieVotesService.generateNonVotedMovieVotes("1000000");

      assertThat(actualMovieVotes).isNotNull();
      assertThat(actualMovieVotes.getId()).isNull();
      assertThat(actualMovieVotes.getMovieId()).isNotNull().isEqualTo("1000000");
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
    @DisplayName("generateNonVotedMovieVotes must throw MovieNotFoundException when not found Movie with given movieId")
    void generateNonVotedMovieVotes_MustThrowMovieNotFoundException_WhenNotFoundMovieWithGivenMovieId() {
      when(movieClientMock.findById("1000000"))
          .thenThrow(new MovieNotFoundException("movie not found with id: 1000000"));

      assertThatExceptionOfType(MovieNotFoundException.class)
          .isThrownBy(() -> movieVotesService.generateNonVotedMovieVotes("1000000"))
          .withMessage("movie not found with id: 1000000");
    }

  }

}