package br.com.emendes.yourreviewapi.exception;

/**
 * Exception a ser usada quando review não for encontrada.
 */
public class ReviewNotFoundException extends RuntimeException {

  public ReviewNotFoundException(String message) {
    super(message);
  }

}
