package br.com.emendes.yourreviewapi.util.faker;

import br.com.emendes.yourreviewapi.dto.response.MovieDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.MovieSummaryResponse;
import br.com.emendes.yourreviewapi.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe para manter objetos fake relacionados a Movie para uso em testes automatizados.
 */
public class MovieFaker {

  public static final String MOVIE_ID = "1234";
  public static final String MOVIE_TITLE = "XPTO";
  public static final String MOVIE_OVERVIEW = "Lorem ipsum dolor sit amet";
  public static final String MOVIE_POSTER_PATH = "/1234";
  public static final String MOVIE_BACKDROP_PATH = "/01234";
  public static final LocalDate MOVIE_RELEASE_DATE = LocalDate.parse("2024-01-16");
  public static final String MOVIE_ORIGINAL_LANGUAGE = "en";

  private MovieFaker() {
  }

  /**
   * Retorna um Movie com todos os campos.
   */
  public static Movie movie() {
    return Movie.builder()
        .id(MOVIE_ID)
        .title(MOVIE_TITLE)
        .overview(MOVIE_OVERVIEW)
        .posterPath(MOVIE_POSTER_PATH)
        .backdropPath(MOVIE_BACKDROP_PATH)
        .releaseDate(MOVIE_RELEASE_DATE)
        .originalLanguage(MOVIE_ORIGINAL_LANGUAGE)
        .build();
  }

  /**
   * Retorna {@code Page<Movie>} com um elemento ({@link MovieFaker#movie()}), Pageable.page = 1 e Pageable.size = 20, e total = 1
   */
  public static Page<Movie> moviePage() {
    return new PageImpl<>(List.of(movie()), PageRequest.of(0, 20), 1);
  }

  /**
   * Retorna MovieSummaryResponse com os mesmos dados de {@link MovieFaker#movie()}.
   */
  public static MovieSummaryResponse movieSummaryResponse() {
    return MovieSummaryResponse.builder()
        .id(MOVIE_ID)
        .title(MOVIE_TITLE)
        .posterPath(MOVIE_POSTER_PATH)
        .releaseDate(MOVIE_RELEASE_DATE)
        .build();
  }

  /**
   * Retorna MovieDetailsResponse com os mesmos dados de {@link MovieFaker#movie()}.
   */
  public static MovieDetailsResponse movieDetailsResponse() {
    return MovieDetailsResponse.builder()
        .id(MOVIE_ID)
        .title(MOVIE_TITLE)
        .overview(MOVIE_OVERVIEW)
        .posterPath(MOVIE_POSTER_PATH)
        .backdropPath(MOVIE_BACKDROP_PATH)
        .releaseDate(MOVIE_RELEASE_DATE)
        .originalLanguage(MOVIE_ORIGINAL_LANGUAGE)
        .build();
  }

  /**
   * Retorna {@code Page<Movie>} empty.
   */
  public static Page<Movie> emptyPage() {
    return new PageImpl<>(List.of(), PageRequest.of(0, 20), 0);
  }

}
