package br.com.emendes.yourreviewapi.controller;

import br.com.emendes.yourreviewapi.dto.request.ReviewRegisterRequest;
import br.com.emendes.yourreviewapi.dto.request.ReviewUpdateRequest;
import br.com.emendes.yourreviewapi.dto.response.ReviewDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewSummaryResponse;
import br.com.emendes.yourreviewapi.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static br.com.emendes.yourreviewapi.config.beans.OpenAPIBeans.SECURITY_SCHEME_KEY;

/**
 * Classe controller reponsável pelo endpoint /api/v1/reviews/**.
 */
@Tag(name = "Review", description = "Review management APIs")
@SecurityRequirement(name = SECURITY_SCHEME_KEY)
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
  @Operation(
      summary = "Register review",
      description = """
          Endpoint to register review, the client can only register one review by movie.
          \nThe response, if successful, has status 201 with a JSON containing information about review registered.
          \nThe client must be authenticated.
          """
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successful register review",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponse.class))),
      @ApiResponse(responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized: jwt authentication failed",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
  })
  @PostMapping
  public ResponseEntity<ReviewResponse> register(
      @RequestBody ReviewRegisterRequest reviewRegisterRequest,
      UriComponentsBuilder uriBuilder) {
    ReviewResponse reviewResponse = reviewService.register(reviewRegisterRequest);
    URI uri = uriBuilder.path("/api/v1/reviews/{id}").build(reviewResponse.id());

    return ResponseEntity.created(uri).body(reviewResponse);
  }

  /**
   * Método responsável por GET /api/v1/reviews.
   *
   * @param movieId identificador do filme relacionado as reviews.
   * @param page    página a ser buscada.
   */
  @Operation(
      summary = "Fetch reviews by movie id",
      description = """
          Endpoint to fetch review by movie id.
          \nThe response, if successful, has status 200 with a JSON containing a pageable list of reviews.
          \nThe client must be authenticated.
          """
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful fetch reviews for given movieId",
          content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized: jwt authentication failed",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
  })
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
  @Operation(
      summary = "Request review by id",
      description = """
          Endpoint to request review by id.
          \nThe response, if successful, has status 200 with a JSON containing detailed review information.
          \nThe client must be authenticated.
          """
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful found review by id",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewDetailsResponse.class))),
      @ApiResponse(responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized: jwt authentication failed",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(responseCode = "404", description = "Review not found",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
  })
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
  @Operation(
      summary = "Update review by id",
      description = """
          Endpoint to update review by id, only the user who registered the review can update it.
          \nThe response, if successful, has status 200 with a JSON containing information about review updated.
          \nThe client must be authenticated.
          """
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful update review by id",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponse.class))),
      @ApiResponse(responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized: jwt authentication failed",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(responseCode = "404", description = "Review not found",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
  })
  @PutMapping("/{reviewId}")
  public ResponseEntity<ReviewResponse> updateById(
      @PathVariable("reviewId") Long reviewId,
      @RequestBody ReviewUpdateRequest reviewUpdateRequest) {
    return ResponseEntity.ok(reviewService.updateById(reviewId, reviewUpdateRequest));
  }

  /**
   * Método responsável por DELETE /api/v1/reviews/{reviewId}.
   *
   * @param reviewId identificador da review a ser deletada.
   */
  @Operation(
      summary = "Delete review by id",
      description = """
          Endpoint to delete review by id, only the user who registered the review can delete it..
          \nThe response, if successful, has status 204.
          \nThe client must be authenticated.
          """
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Successful delete review by id",
          content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized: jwt authentication failed",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(responseCode = "404", description = "Review not found",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
  })
  @DeleteMapping("/{reviewId}")
  public ResponseEntity<Void> deleteById(@PathVariable("reviewId") Long reviewId) {
    reviewService.deleteById(reviewId);
    return ResponseEntity.noContent().build();
  }

}
