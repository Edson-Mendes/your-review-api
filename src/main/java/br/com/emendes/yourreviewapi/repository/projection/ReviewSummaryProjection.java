package br.com.emendes.yourreviewapi.repository.projection;

/**
 * Interface projection usado para buscar apenas os dados necessários para resumir uma Review.
 */
public interface ReviewSummaryProjection {

  Long getId();

  int getVote();

  String getOpinion();

  Long getUserId();

  String getUserName();

  String getUserEmail();

  String getMovieVotesMovieId();

}
