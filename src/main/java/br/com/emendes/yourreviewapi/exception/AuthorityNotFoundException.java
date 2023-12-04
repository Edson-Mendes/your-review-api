package br.com.emendes.yourreviewapi.exception;

/**
 * Exception a ser lançada quando Authority não for encontrada.
 */
public class AuthorityNotFoundException extends RuntimeException {

  public AuthorityNotFoundException(String message) {
    super(message);
  }

}
