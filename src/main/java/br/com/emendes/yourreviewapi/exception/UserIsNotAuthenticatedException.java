package br.com.emendes.yourreviewapi.exception;

/**
 * Exception a ser lançada não houver usuário autenticado.
 */
public class UserIsNotAuthenticatedException extends RuntimeException {

  public UserIsNotAuthenticatedException(String message) {
    super(message);
  }

}
