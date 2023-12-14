package br.com.emendes.yourreviewapi.exception;

/**
 * Exception a ser lançada houver colisão de email.
 */
public class EmailAlreadyInUseException extends RuntimeException {

  public EmailAlreadyInUseException(String message) {
    super(message);
  }

}
