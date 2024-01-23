package br.com.emendes.yourreviewapi.exception;

import lombok.Getter;

/**
 * Exception a ser lançada quando MovieVotes não for encontrado.
 */
@Getter
public class MovieVotesNotFoundException extends RuntimeException {

  /**
   * Status code que deve ser devolvido em caso dessa exception ocorrer.
   */
  private final int statusCode;

  public MovieVotesNotFoundException(String message) {
    this(message, 404);
  }

  public MovieVotesNotFoundException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }
}
