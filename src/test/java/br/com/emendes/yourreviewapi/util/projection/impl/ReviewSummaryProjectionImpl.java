package br.com.emendes.yourreviewapi.util.projection.impl;

import br.com.emendes.yourreviewapi.repository.projection.ReviewSummaryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Implementação de {@link ReviewSummaryProjection} para ser usado em testes automatizados.
 */
@Builder
@AllArgsConstructor
public class ReviewSummaryProjectionImpl implements ReviewSummaryProjection {

  private Long id;
  private int vote;
  private String opinion;
  private Long userId;
  private String movieVotesMovieId;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public int getVote() {
    return vote;
  }

  @Override
  public String getOpinion() {
    return opinion;
  }

  @Override
  public Long getUserId() {
    return userId;
  }

  @Override
  public String getMovieVotesMovieId() {
    return movieVotesMovieId;
  }
}
