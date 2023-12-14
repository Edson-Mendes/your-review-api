package br.com.emendes.yourreviewapi.exception;

/**
 * Exception a ser lançada quando passwords não corresponderem.
 */
public class PasswordsDoesNotMatchException extends RuntimeException {

  public PasswordsDoesNotMatchException(String message) {
    super(message);
  }

}
