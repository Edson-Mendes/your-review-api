package br.com.emendes.yourreviewapi.util.projection.impl;

import br.com.emendes.yourreviewapi.repository.projection.ReviewDetailsProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Implementação de {@link ReviewDetailsProjection} para ser usado em testes automatizados.
 */
@AllArgsConstructor
@Builder
public class ReviewDetailsProjectionImpl implements ReviewDetailsProjection {

  private Long id;
  private int vote;
  private String opinion;
  private LocalDateTime createdAt;
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
  public LocalDateTime getCreatedAt() {
    return createdAt;
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
