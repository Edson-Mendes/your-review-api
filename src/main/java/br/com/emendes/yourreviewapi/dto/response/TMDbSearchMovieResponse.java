package br.com.emendes.yourreviewapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Response para busca de filmes na API do TMDb.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class TMDbSearchMovieResponse {

  private int page;
  private List<TMDbMovieResponse> results;
  @JsonProperty("total_pages")
  private int totalPages;
  @JsonProperty("total_results")
  private int totalResults;

}
