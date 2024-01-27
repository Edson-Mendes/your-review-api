package br.com.emendes.yourreviewapi.controller;

import br.com.emendes.yourreviewapi.dto.request.ReviewRegisterRequest;
import br.com.emendes.yourreviewapi.dto.response.ReviewDetailsResponse;
import br.com.emendes.yourreviewapi.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

}
