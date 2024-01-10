package br.com.emendes.yourreviewapi.exception;

/**
 * Exception a ser lançada quando Movie não for encontrado.
 */
public class MovieNotFoundException extends RuntimeException {

  public MovieNotFoundException(String message) {
    super(message);
  }

}
