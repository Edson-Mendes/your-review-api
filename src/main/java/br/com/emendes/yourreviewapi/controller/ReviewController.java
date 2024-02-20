package br.com.emendes.yourreviewapi.controller;

import br.com.emendes.yourreviewapi.dto.request.ReviewRegisterRequest;
import br.com.emendes.yourreviewapi.dto.request.ReviewUpdateRequest;
import br.com.emendes.yourreviewapi.dto.response.ReviewDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewSummaryResponse;
import br.com.emendes.yourreviewapi.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Classe controller reponsável pelo endpoint /api/v1/reviews/**.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

  private final ReviewService reviewService;

  /**
   * Método responsável por POST /api/v1/reviews.
   *
   * @param reviewRegisterRequest dto com os dados de cadastro de Review.
   * @param uriBuilder            objeto UriComponentsBuilder que mantém o host do endpoint para construção do header
   *                              location do recurso criado.
   */
  @PostMapping
  public ResponseEntity<ReviewDetailsResponse> register(
      @RequestBody ReviewRegisterRequest reviewRegisterRequest,
      UriComponentsBuilder uriBuilder) {
    ReviewDetailsResponse reviewDetailsResponse = reviewService.register(reviewRegisterRequest);
    URI uri = uriBuilder.path("/api/v1/reviews/{id}").build(reviewDetailsResponse.id());

    return ResponseEntity.created(uri).body(reviewDetailsResponse);
  }

  /**
   * Método responsável por GET /api/v1/reviews.
   *
   * @param movieId identificador do filme relacionado as reviews.
   * @param page    página a ser buscada.
   */
  @GetMapping
  public ResponseEntity<Page<ReviewSummaryResponse>> fetchByMovieId(
      @RequestParam("movieId") String movieId,
      @RequestParam(value = "page", required = false, defaultValue = "0") int page) {
    return ResponseEntity.ok(reviewService.fetchByMovieId(movieId, page));
  }

  /**
   * Método responsável por GET /api/v1/reviews/{reviewId}.
   *
   * @param reviewId identificador da review a ser buscada.
   */
  @GetMapping("/{reviewId}")
  public ResponseEntity<ReviewDetailsResponse> findById(@PathVariable("reviewId") Long reviewId) {
    return ResponseEntity.ok(reviewService.findById(reviewId));
  }

  /**
   * Método responsável por PUT /api/v1/reviews/{reviewId}.
   *
   * @param reviewId            identificador da review a ser atualizada.
   * @param reviewUpdateRequest objeto com os novos dados da Review.
   */
  @PutMapping("/{reviewId}")
  public ResponseEntity<ReviewDetailsResponse> updateById(
      @PathVariable("reviewId") Long reviewId,
      @RequestBody ReviewUpdateRequest reviewUpdateRequest) {
    return ResponseEntity.ok(reviewService.updateById(reviewId, reviewUpdateRequest));
  }

}
