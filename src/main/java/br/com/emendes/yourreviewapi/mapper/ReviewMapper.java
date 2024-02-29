package br.com.emendes.yourreviewapi.mapper;

import br.com.emendes.yourreviewapi.dto.request.ReviewRegisterRequest;
import br.com.emendes.yourreviewapi.dto.request.ReviewUpdateRequest;
import br.com.emendes.yourreviewapi.dto.response.MovieSummaryResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewSummaryResponse;
import br.com.emendes.yourreviewapi.model.Movie;
import br.com.emendes.yourreviewapi.model.entity.Review;
import br.com.emendes.yourreviewapi.repository.projection.ReviewDetailsProjection;
import br.com.emendes.yourreviewapi.repository.projection.ReviewSummaryProjection;

/**
 * Interface com as abstrações para mapeamento do recurso {@link Review}.
 */
public interface ReviewMapper {

  /**
   * Mapeia um objeto {@link ReviewRegisterRequest} para {@link Review}.
   *
   * @param reviewRegisterRequest objeto a ser mapeado para Review.
   * @return Objeto Review com dados de ReviewRegisterRequest.
   * @throws IllegalArgumentException caso reviewRegisterRequest seja null.
   */
  Review toReview(ReviewRegisterRequest reviewRegisterRequest);

  /**
   * Mapeia um objeto {@link Review} para {@link ReviewResponse}.
   *
   * @param review objeto a ser mapeado para ReviewResponse.
   * @return Objeto ReviewResponse com os dados de Review.
   * @throws IllegalArgumentException caso review seja null.
   */
  ReviewResponse toReviewResponse(Review review);

  /**
   * Mapeia um objeto {@link ReviewDetailsProjection} para {@link ReviewDetailsResponse}.
   *
   * @param reviewDetailsProjection objeto a ser mapeado para ReviewDetailsResponse.
   * @param movie                   objeto a ser mapeado e adicionado a ReviewDetailsResponse.
   * @return Objeto ReviewDetailsResponse com os dados de ReviewDetailsProjection.
   * @throws IllegalArgumentException caso reviewDetailsProjection seja null.
   */
  ReviewDetailsResponse toReviewDetailsResponse(ReviewDetailsProjection reviewDetailsProjection, MovieSummaryResponse movie);

  /**
   * Mapeia um objeto {@link Review} para {@link ReviewSummaryResponse}.
   *
   * @param reviewSummaryProjection objeto a ser mapeado para ReviewSummaryResponse.
   * @return Objeto ReviewSummaryResponse com os dados de Review.
   * @throws IllegalArgumentException caso review seja null.
   */
  ReviewSummaryResponse toReviewSummaryResponse(ReviewSummaryProjection reviewSummaryProjection);

  /**
   * Mescla as informações de review com reviewUpdateRequest.
   *
   * @param review              objeto que receberá dado de reviewUpdateRequest.
   * @param reviewUpdateRequest objeto com novos dados de review.
   * @throws IllegalArgumentException caso review ou reviewUpdateRequest sejam nulos.
   */
  void merge(Review review, ReviewUpdateRequest reviewUpdateRequest);
}
