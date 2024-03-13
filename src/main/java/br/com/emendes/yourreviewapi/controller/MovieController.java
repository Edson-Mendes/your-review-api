package br.com.emendes.yourreviewapi.controller;

import br.com.emendes.yourreviewapi.dto.response.MovieDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.MovieSummaryResponse;
import br.com.emendes.yourreviewapi.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Classe controller reponsável pelo endpoint /api/v1/movies/**.
 */
@Tag(name = "Movie", description = "Movie management APIs")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

  private final MovieService movieService;

  /**
   * Método responsável por GET /api/v1/movies.
   *
   * @param name nome do filme a ser buscado.
   * @param page página a ser buscada. Zero-based.
   */
  @Operation(
      summary = "Request movie by name",
      description = """
          Endpoint to request movies by name.
          \nThe response, if successful, has status 200 with a JSON containing a pageable list of movies.
          """
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful search movie by name",
          content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
  })
  @GetMapping
  public ResponseEntity<Page<MovieSummaryResponse>> findByName(
      @RequestParam("name") String name,
      @RequestParam(value = "page", required = false, defaultValue = "0") int page) {
    return ResponseEntity.ok(movieService.findByName(name, page));
  }

  /**
   * Método responsável por GET /api/v1/movies/{id}.
   *
   * @param movieId identificador do filme a ser buscado.
   */
  @Operation(
      summary = "Request movie by id",
      description = """
          Endpoint to request movies by id.
          \nThe response, if successful, has status 200 with a JSON containing detailed movie information.
          """
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful found movie by id",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = MovieDetailsResponse.class))),
      @ApiResponse(responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(responseCode = "404", description = "Movie not found",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
  })
  @GetMapping("/{id}")
  public ResponseEntity<MovieDetailsResponse> findById(
      @PathVariable("id") String movieId) {
    return ResponseEntity.ok(movieService.findDetailedById(movieId));
  }

}
