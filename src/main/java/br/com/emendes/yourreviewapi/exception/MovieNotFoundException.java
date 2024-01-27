package br.com.emendes.yourreviewapi.exception;

import lombok.Getter;

/**
 * Exception a ser lançada quando Movie não for encontrado.
 */
@Getter
public class MovieNotFoundException extends RuntimeException {

  private final int statusCode;

  public MovieNotFoundException(String message) {
    this(message, 404);
  }

  public MovieNotFoundException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }
}
