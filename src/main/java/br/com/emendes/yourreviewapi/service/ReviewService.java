package br.com.emendes.yourreviewapi.service;

import br.com.emendes.yourreviewapi.dto.request.ReviewRegisterRequest;
import br.com.emendes.yourreviewapi.dto.request.ReviewUpdateRequest;
import br.com.emendes.yourreviewapi.dto.response.ReviewDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewSummaryResponse;
import br.com.emendes.yourreviewapi.exception.ReviewNotFoundException;
import br.com.emendes.yourreviewapi.model.entity.Review;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

/**
 * Interface service com as abstrações para manipulação deo recurso Review.
 */
@Validated
public interface ReviewService {

  /**
   * Cadastrar uma Review no sistema.
   *
   * @param reviewRegisterRequest objeto DTO contendo os dados para criação da Review.
   * @return ReviewDetailResponse contendo informações detalhadas da Review cadastrada.
   */
  ReviewResponse register(@Valid ReviewRegisterRequest reviewRegisterRequest);


  /**
   * Buscar paginada de {@link Review} por movieId.
   *
   * @param movieId identificador do filme.
   * @param page    página a ser buscada.
   * @return {@code Page<ReviewSummaryResponse>} objeto paginado com as reviews encontradas.
   */
  Page<ReviewSummaryResponse> fetchByMovieId(
      @NotBlank(message = "{ReviewService.fetchByMovieId.movieId.NotBlank.message}") String movieId,
      @PositiveOrZero(message = "{ReviewService.fetchByMovieId.page.PositiveOrZero.message}") int page);

  /**
   * Buscar {@link Review} por id.
   *
   * @param reviewId identificador da review a ser buscada.
   * @return Objeto {@link ReviewResponse} contendo informações da Review.
   * @throws ReviewNotFoundException caso não seja encontrada review para o dado reviewId.
   */
  ReviewDetailsResponse findById(@NotNull(message = "{ReviewService.findById.reviewId.NotNull.message}") Long reviewId);

  /**
   * Atualiza {@link Review} por id.
   *
   * @param reviewId            identificador da review a ser atualizada.
   * @param reviewUpdateRequest objeto com os novos dados da Review.
   * @return Objeto ReviewResponse com os dados atualizados.
   * @throws ReviewNotFoundException caso não seja encontrada review para o dado reviewId.
   */
  ReviewResponse updateById(
      @NotNull(message = "{ReviewService.findById.reviewId.NotNull.message}") Long reviewId,
      @Valid ReviewUpdateRequest reviewUpdateRequest);

  /**
   * Deleta {@link Review} por id. A review a ser deletada deve estar relacionado com o usuário atual da requisição.
   *
   * @param reviewId identificador da Review a ser deletada.
   * @throws ReviewNotFoundException caso não seja encontrada Review para o dado {@code reviewId}.
   */
  void deleteById(@NotNull(message = "{ReviewService.deleteById.reviewId.NotNull.message}") Long reviewId);

}
