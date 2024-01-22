package br.com.emendes.yourreviewapi.util.faker;

import br.com.emendes.yourreviewapi.dto.response.TMDbMovieResponse;

import java.time.LocalDate;

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
        "1000000", "Lorem",
        "Lorem ipsum dolor sit amet",
        LocalDate.parse("2024-01-16"), "/1000000", "en");
  }
}
