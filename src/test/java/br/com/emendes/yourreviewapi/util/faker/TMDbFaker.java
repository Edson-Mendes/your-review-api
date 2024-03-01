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
    return new TMDbMovieResponse(
        MOVIE_ID, MOVIE_TITLE, MOVIE_OVERVIEW,
        MOVIE_RELEASE_DATE, MOVIE_POSTER_PATH, MOVIE_ORIGINAL_LANGUAGE);
  }
}
