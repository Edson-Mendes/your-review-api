package br.com.emendes.yourreviewapi.service;

import br.com.emendes.yourreviewapi.dto.request.ReviewRegisterRequest;
import br.com.emendes.yourreviewapi.dto.response.ReviewDetailsResponse;
import jakarta.validation.Valid;
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
  ReviewDetailsResponse register(@Valid ReviewRegisterRequest reviewRegisterRequest);

}
