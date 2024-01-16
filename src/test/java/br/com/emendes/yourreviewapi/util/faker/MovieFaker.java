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

  private MovieFaker() {
  }

  /**
   * Retorna um Movie com todos os campos.
   */
  public static Movie movie() {
    return Movie.builder()
        .id("1000000")
        .title("Lorem")
        .posterPath("/1000000")
        .overview("Lorem ipsum dolor sit amet")
        .releaseDate(LocalDate.parse("2024-01-16"))
        .originalLanguage("en")
        .build();
  }

  /**
   * Retorna {@code Page<Movie>} com um elemento ({@link MovieFaker#movie()}), Pageable.page = 1 e Pageable.size = 20, e total = 1
   */
  public static Page<Movie> moviePage() {
    return new PageImpl<>(List.of(movie()), PageRequest.of(1, 20), 1);
  }

  /**
   * Retorna MovieSummaryResponse com os mesmos dados de {@link MovieFaker#movie()}.
   */
  public static MovieSummaryResponse movieSummaryResponse() {
    return MovieSummaryResponse.builder()
        .id("1000000")
        .title("Lorem")
        .posterPath("/1000000")
        .releaseDate(LocalDate.parse("2024-01-16"))
        .build();
  }

  /**
   * Retorna MovieDetailsResponse com os mesmos dados de {@link MovieFaker#movie()}.
   */
  public static MovieDetailsResponse movieDetailsResponse() {
    return MovieDetailsResponse.builder()
        .id("1000000")
        .title("Lorem")
        .posterPath("/1000000")
        .overview("Lorem ipsum dolor sit amet")
        .releaseDate(LocalDate.parse("2024-01-16"))
        .originalLanguage("en")
        .build();
  }

}
