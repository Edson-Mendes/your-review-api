package br.com.emendes.yourreviewapi.unit.mapper.impl;

import br.com.emendes.yourreviewapi.dto.response.MovieDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.MovieSummaryResponse;
import br.com.emendes.yourreviewapi.dto.response.TMDbMovieResponse;
import br.com.emendes.yourreviewapi.mapper.impl.MovieMapperImpl;
import br.com.emendes.yourreviewapi.model.Movie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Unit tests de MovieMapperImpl")
class MovieMapperImplTest {

  private final MovieMapperImpl movieMapper = new MovieMapperImpl();

  @Test
  @DisplayName("toMovieSummaryResponse must return MovieSummaryResponse when map successfully")
  void toMovieSummaryResponse_MustReturnMovieSummaryResponse_WhenMapSuccessfully() {
    Movie movie = Movie.builder()
        .id("1234")
        .title("XPTO")
        .posterPath("/1234")
        .backdropPath("/01234")
        .overview("Lorem ipsum dolor sit amet")
        .releaseDate(LocalDate.parse("2024-01-16"))
        .originalLanguage("en")
        .build();

    MovieSummaryResponse actualMovieSummaryResponse = movieMapper.toMovieSummaryResponse(movie);

    assertThat(actualMovieSummaryResponse).isNotNull();
    assertThat(actualMovieSummaryResponse.id()).isNotNull().isEqualTo("1234");
    assertThat(actualMovieSummaryResponse.title()).isNotNull().isEqualTo("XPTO");
    assertThat(actualMovieSummaryResponse.releaseDate()).isNotNull().isEqualTo("2024-01-16");
    assertThat(actualMovieSummaryResponse.posterPath()).isNotNull().isEqualTo("/1234");
  }

  @Test
  @DisplayName("toMovieSummaryResponse must throw IllegalArgumentException when movie parameter is null")
  void toMovieSummaryResponse_MustThrowIllegalArgumentException_WhenMovieParameterIsNull() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> movieMapper.toMovieSummaryResponse(null))
        .withMessage("movie must not be null");
  }

  @Test
  @DisplayName("toMovieDetailsResponse must return MovieDetailsResponse when map successfully")
  void toMovieDetailsResponse_MustReturnMovieDetailsResponse_WhenMapSuccessfully() {
    Movie movie = Movie.builder()
        .id("1234")
        .title("XPTO")
        .posterPath("/1234")
        .backdropPath("/01234")
        .overview("Lorem ipsum dolor sit amet")
        .releaseDate(LocalDate.parse("2024-01-16"))
        .originalLanguage("en")
        .build();

    MovieDetailsResponse actualMovieDetailsResponse = movieMapper.toMovieDetailsResponse(movie);

    assertThat(actualMovieDetailsResponse).isNotNull();
    assertThat(actualMovieDetailsResponse.id()).isNotNull().isEqualTo("1234");
    assertThat(actualMovieDetailsResponse.title()).isNotNull().isEqualTo("XPTO");
    assertThat(actualMovieDetailsResponse.releaseDate()).isNotNull().isEqualTo("2024-01-16");
    assertThat(actualMovieDetailsResponse.posterPath()).isNotNull().isEqualTo("/1234");
    assertThat(actualMovieDetailsResponse.backdropPath()).isNotNull().isEqualTo("/01234");
    assertThat(actualMovieDetailsResponse.originalLanguage()).isNotNull().isEqualTo("en");
    assertThat(actualMovieDetailsResponse.overview()).isNotNull().isEqualTo("Lorem ipsum dolor sit amet");
  }

  @Test
  @DisplayName("toMovieDetailsResponse must throw IllegalArgumentException when movie parameter is null")
  void toMovieDetailsResponse_MustThrowIllegalArgumentException_WhenMovieParameterIsNull() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> movieMapper.toMovieDetailsResponse(null))
        .withMessage("movie must not be null");
  }

  @Test
  @DisplayName("toMovie must return Movie when map successfully")
  void toMovie_MustReturnMovie_WhenMapSuccessfully() {
    TMDbMovieResponse tmDbMovieResponse = TMDbMovieResponse.builder()
        .id("1234")
        .title("XPTO")
        .overview("Lorem ipsum dolor sit amet")
        .releaseDate(LocalDate.parse("2024-01-16"))
        .posterPath("/1234")
        .backdropPath("/01234")
        .originalLanguage("en")
        .build();

    Movie actualMovie = movieMapper.toMovie(tmDbMovieResponse);

    assertThat(actualMovie).isNotNull();
    assertThat(actualMovie.id()).isNotNull().isEqualTo("1234");
    assertThat(actualMovie.title()).isNotNull().isEqualTo("XPTO");
    assertThat(actualMovie.releaseDate()).isNotNull().isEqualTo("2024-01-16");
    assertThat(actualMovie.posterPath()).isNotNull().isEqualTo("/1234");
    assertThat(actualMovie.backdropPath()).isNotNull().isEqualTo("/01234");
    assertThat(actualMovie.originalLanguage()).isNotNull().isEqualTo("en");
    assertThat(actualMovie.overview()).isNotNull().isEqualTo("Lorem ipsum dolor sit amet");
  }

  @Test
  @DisplayName("toMovie must throw IllegalArgumentException when tmdbMovieResponse parameter is null")
  void toMovie_MustThrowIllegalArgumentException_WhenTmdbMovieResponseParameterIsNull() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> movieMapper.toMovie(null))
        .withMessage("tmdbMovieResponse must not be null");
  }

}