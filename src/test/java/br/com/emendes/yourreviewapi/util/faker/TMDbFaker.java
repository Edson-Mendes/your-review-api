package br.com.emendes.yourreviewapi.util.faker;

import br.com.emendes.yourreviewapi.dto.response.TMDbMovieResponse;

import static br.com.emendes.yourreviewapi.util.faker.MovieFaker.*;

/**
 * Classe para manter objetos fake relacionados a TMDb response para uso em testes automatizados.
 */
public final class TMDbFaker {

  private TMDbFaker() {
  }

  /**
   * Retorna uma instância de TMDbMovieResponse com todos os campos.
   */
  public static TMDbMovieResponse tMDbMovieResponse() {
    return TMDbMovieResponse.builder()
        .id(MOVIE_ID)
        .title(MOVIE_TITLE)
        .overview(MOVIE_OVERVIEW)
        .releaseDate(MOVIE_RELEASE_DATE)
        .posterPath(MOVIE_POSTER_PATH)
        .backdropPath(MOVIE_BACKDROP_PATH)
        .originalLanguage(MOVIE_ORIGINAL_LANGUAGE)
        .build();
  }
}
