package br.com.emendes.yourreviewapi.unit.service.impl;

import br.com.emendes.yourreviewapi.client.MovieClient;
import br.com.emendes.yourreviewapi.dto.response.MovieDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.MovieSummaryResponse;
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

      Assertions.assertThat(actualMovieSummaryResponsePage).isNotNull().hasSize(1);
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

      Assertions.assertThat(actualMovieDetailsResponse).isNotNull();
      Assertions.assertThat(actualMovieDetailsResponse.id()).isNotNull().isEqualTo("1234");
      Assertions.assertThat(actualMovieDetailsResponse.title()).isNotNull().isEqualTo("XPTO");
      Assertions.assertThat(actualMovieDetailsResponse.overview()).isNotNull()
          .isEqualTo("Lorem ipsum dolor sit amet");
      Assertions.assertThat(actualMovieDetailsResponse.posterPath()).isNotNull().isEqualTo("/1234");
      Assertions.assertThat(actualMovieDetailsResponse.releaseDate()).isNotNull().isEqualTo("2024-01-16");
      Assertions.assertThat(actualMovieDetailsResponse.originalLanguage()).isNotNull().isEqualTo("en");
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

      Assertions.assertThat(actualMovieSummaryResponse).isNotNull();
      Assertions.assertThat(actualMovieSummaryResponse.id()).isNotNull().isEqualTo("1234");
      Assertions.assertThat(actualMovieSummaryResponse.title()).isNotNull().isEqualTo("XPTO");
      Assertions.assertThat(actualMovieSummaryResponse.posterPath()).isNotNull().isEqualTo("/1234");
      Assertions.assertThat(actualMovieSummaryResponse.releaseDate()).isNotNull().isEqualTo("2024-01-16");
    }

    @Test
    @DisplayName("findSummarizedById must throw MovieNotFoundException when not found Movie for given movieId")
    void findSummarizedById_MustThrowMovieNotFoundException_WhenNotFoundMovieForGivenMovieId() {
      when(movieClientMock.findById("1234")).thenThrow(new MovieNotFoundException("movie not found with id: 1234"));

      Assertions.assertThatExceptionOfType(MovieNotFoundException.class)
          .isThrownBy(() -> movieService.findSummarizedById("1234"))
          .withMessage("movie not found with id: 1234");
    }

  }

}