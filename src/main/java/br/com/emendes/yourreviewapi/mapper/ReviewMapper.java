package br.com.emendes.yourreviewapi.mapper;

import br.com.emendes.yourreviewapi.dto.request.ReviewRegisterRequest;
import br.com.emendes.yourreviewapi.dto.response.ReviewDetailsResponse;
import br.com.emendes.yourreviewapi.model.entity.Review;

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
   * Mapeia um objeto {@link Review} para {@link ReviewDetailsResponse}.
   *
   * @param review objeto a ser mapeado para ReviewDetailsResponse.
   * @return Objeto ReviewDetailsResponse com os dados de Review.
   * @throws IllegalArgumentException caso review seja null.
   */
  ReviewDetailsResponse toReviewDetailsResponse(Review review);

}
