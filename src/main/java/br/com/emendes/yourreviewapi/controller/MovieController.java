package br.com.emendes.yourreviewapi.controller;

import br.com.emendes.yourreviewapi.dto.response.MovieSummaryResponse;
import br.com.emendes.yourreviewapi.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Classe controller reponsável pelo endpoint /api/v1/movies/**.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

  private final MovieService movieService;

  /**
   * Método responsável por GET /api/v1/movies.
   *
   * @param name nome do filme a ser buscado.
   * @param page página a ser buscada.
   */
  @GetMapping
  public ResponseEntity<Page<MovieSummaryResponse>> findByName(
      @RequestParam("name") String name,
      @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
    return ResponseEntity.ok(movieService.findByName(name, page));
  }

}
