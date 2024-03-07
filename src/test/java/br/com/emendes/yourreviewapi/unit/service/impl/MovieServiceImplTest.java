package br.com.emendes.yourreviewapi.unit.service.impl;

import br.com.emendes.yourreviewapi.client.MovieClient;
import br.com.emendes.yourreviewapi.dto.response.MovieDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.MovieSummaryResponse;
import br.com.emendes.yourreviewapi.exception.InvalidTMDbApiKeyException;
import br.com.emendes.yourreviewapi.exception.MovieNotFoundException;
import br.com.emendes.yourreviewapi.mapper.MovieMapper;
import br.com.emendes.yourreviewapi.model.Movie;
import br.com.emendes.yourreviewapi.service.impl.MovieServiceImpl;
import br.com.emendes.yourreviewapi.util.faker.MovieFaker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for MovieServiceImpl")
class MovieServiceImplTest {

  @InjectMocks
  private MovieServiceImpl movieService;
  @Mock
  private MovieClient movieClientMock;
  @Mock
  private MovieMapper movieMapperMock;

  @Nested
  @DisplayName("Tests for findByName method")
  class FindByNameMethod {

    @Test
    @DisplayName("findByName must return Page<MovieSummaryResponse> when found successfully")
    void findByName_MustReturnPageMovieSummaryResponse_WhenFoundSuccessfully() {
      when(movieClientMock.findByName("XPTO", 1))
          .thenReturn(MovieFaker.moviePage());
      when(movieMapperMock.toMovieSummaryResponse(any(Movie.class)))
          .thenReturn(MovieFaker.movieSummaryResponse());

      Page<MovieSummaryResponse> actualMovieSummaryResponsePage = movieService.findByName("XPTO", 1);

      assertThat(actualMovieSummaryResponsePage).isNotNull().hasSize(1);
    }

    @Test
    @DisplayName("findByName must throw InvalidTMDbApiKeyException when api_key is invalid")
    void findByName_MustThrowInvalidTMDbApiKeyException_WhenAPI_KEYIsInvalid() {
      when(movieClientMock.findByName("XPTO", 1))
          .thenThrow(new InvalidTMDbApiKeyException("invalid TMDb API Key"));

      assertThatExceptionOfType(InvalidTMDbApiKeyException.class)
          .isThrownBy(() -> movieService.findByName("XPTO", 1));
    }

  }

  @Nested
  @DisplayName("Tests for findDetailedById method")
  class FindDetailedByIdMethod {

    @Test
    @DisplayName("findDetailedById must return MovieDetailsResponse when found successfully")
    void findDetailedById_MustReturnMovieDetailsResponse_WhenFoundSuccessfully() {
      when(movieClientMock.findById("1234"))
          .thenReturn(MovieFaker.movie());
      when(movieMapperMock.toMovieDetailsResponse(any(Movie.class)))
          .thenReturn(MovieFaker.movieDetailsResponse());

      MovieDetailsResponse actualMovieDetailsResponse = movieService.findDetailedById("1234");

      assertThat(actualMovieDetailsResponse).isNotNull();
      assertThat(actualMovieDetailsResponse.id()).isNotNull().isEqualTo("1234");
      assertThat(actualMovieDetailsResponse.title()).isNotNull().isEqualTo("XPTO");
      assertThat(actualMovieDetailsResponse.overview()).isNotNull()
          .isEqualTo("Lorem ipsum dolor sit amet");
      assertThat(actualMovieDetailsResponse.posterPath()).isNotNull().isEqualTo("/1234");
      assertThat(actualMovieDetailsResponse.backdropPath()).isNotNull().isEqualTo("/01234");
      assertThat(actualMovieDetailsResponse.releaseDate()).isNotNull().isEqualTo("2024-01-16");
      assertThat(actualMovieDetailsResponse.originalLanguage()).isNotNull().isEqualTo("en");
    }

    @Test
    @DisplayName("findDetailedById must throw InvalidTMDbApiKeyException when api_key is invalid")
    void findDetailedById_MustThrowInvalidTMDbApiKeyException_WhenAPI_KEYIsInvalid() {
      when(movieClientMock.findById("1234"))
          .thenThrow(new InvalidTMDbApiKeyException("invalid TMDb API Key"));

      Assertions.assertThatExceptionOfType(InvalidTMDbApiKeyException.class)
          .isThrownBy(() -> movieService.findDetailedById("1234"))
          .withMessage("invalid TMDb API Key");
    }

    @Test
    @DisplayName("findDetailedById must throw MovieNotFoundException when not found Movie for given movieId")
    void findDetailedById_MustThrowMovieNotFoundException_WhenNotFoundMovieForGivenMovieId() {
      when(movieClientMock.findById("1234")).thenThrow(new MovieNotFoundException("movie not found with id: 1234"));

      Assertions.assertThatExceptionOfType(MovieNotFoundException.class)
          .isThrownBy(() -> movieService.findDetailedById("1234"))
          .withMessage("movie not found with id: 1234");
    }

  }

  @Nested
  @DisplayName("Tests for findSummarizedById method")
  class FindSummarizedByIdMethod {

    @Test
    @DisplayName("findSummarizedById must return MovieSummaryResponse when found successfully")
    void findSummarizedById_MustReturnMovieSummaryResponse_WhenFoundSuccessfully() {
      when(movieClientMock.findById("1234"))
          .thenReturn(MovieFaker.movie());
      when(movieMapperMock.toMovieSummaryResponse(any(Movie.class)))
          .thenReturn(MovieFaker.movieSummaryResponse());

      MovieSummaryResponse actualMovieSummaryResponse = movieService.findSummarizedById("1234");

      assertThat(actualMovieSummaryResponse).isNotNull();
      assertThat(actualMovieSummaryResponse.id()).isNotNull().isEqualTo("1234");
      assertThat(actualMovieSummaryResponse.title()).isNotNull().isEqualTo("XPTO");
      assertThat(actualMovieSummaryResponse.posterPath()).isNotNull().isEqualTo("/1234");
      assertThat(actualMovieSummaryResponse.releaseDate()).isNotNull().isEqualTo("2024-01-16");
    }

    @Test
    @DisplayName("findSummarizedById must throw MovieNotFoundException when not found Movie for given movieId")
    void findSummarizedById_MustThrowMovieNotFoundException_WhenNotFoundMovieForGivenMovieId() {
      when(movieClientMock.findById("1234")).thenThrow(new MovieNotFoundException("movie not found with id: 1234"));

      Assertions.assertThatExceptionOfType(MovieNotFoundException.class)
          .isThrownBy(() -> movieService.findSummarizedById("1234"))
          .withMessage("movie not found with id: 1234");
    }

    @Test
    @DisplayName("findSummarizedById must throw InvalidTMDbApiKeyException when api_key is invalid")
    void findSummarizedById_MustThrowInvalidTMDbApiKeyException_WhenAPI_KEYIsInvalid() {
      when(movieClientMock.findById("1234"))
          .thenThrow(new InvalidTMDbApiKeyException("invalid TMDb API Key"));

      Assertions.assertThatExceptionOfType(InvalidTMDbApiKeyException.class)
          .isThrownBy(() -> movieService.findSummarizedById("1234"))
          .withMessage("invalid TMDb API Key");
    }

  }

  @Nested
  @DisplayName("Tests for existsMovieById method")
  class ExistsMovieByIdMethod {

    @Test
    @DisplayName("existsMovieById must return true when exists Movie with given movieId")
    void existsMovieById_MustReturnTrue_WhenExistsMovieWithGivenMovieId() {
      when(movieClientMock.findById("1234")).thenReturn(MovieFaker.movie());

      boolean actualExists = movieService.existsMovieById("1234");

      assertThat(actualExists).isTrue();
    }

    @Test
    @DisplayName("existsMovieById must return false when there is no Movie with given movieId")
    void existsMovieById_MustReturnTrue_WhenThereIsNoMovieWithGivenMovieId() {
      when(movieClientMock.findById("1234"))
          .thenThrow(new MovieNotFoundException("Movie not found for id: 1234"));

      boolean actualExists = movieService.existsMovieById("1234");

      assertThat(actualExists).isFalse();
    }

  }

}