package br.com.emendes.yourreviewapi.repository.projection;

import java.time.LocalDateTime;

/**
 * Interface projection usado para buscar apenas os dados necessários para detalhas uma Review.
 */
public interface ReviewDetailsProjection {

  Long getId();

  int getVote();

  String getOpinion();

  LocalDateTime getCreatedAt();

  Long getUserId();

  String getMovieVotesMovieId();

}