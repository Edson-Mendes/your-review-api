package br.com.emendes.yourreviewapi.service;

import br.com.emendes.yourreviewapi.dto.response.MovieSummaryResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

/**
 * Interface service com as abstrações para manipulação de Movies.
 */
@Validated
public interface MovieService {

  /**
   * Busca filmes pelo nome.
   *
   * @param name nome do filme a ser buscado.
   * @param page objeto com as informações de páginação.
   * @return {@code Page<MovieSummaryResponse>} objeto paginado com os filmes encontrados.
   */
  Page<MovieSummaryResponse> findByName(
      @NotBlank(message = "{find.name.notblank}") String name,
      @Positive(message = "{find.page.positive}") int page);

}