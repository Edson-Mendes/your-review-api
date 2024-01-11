package br.com.emendes.yourreviewapi.exception;

/**
 * Exception a ser lançada quando a API key do TMDb for inválida.
 */
public class InvalidTMDbApiKeyException extends RuntimeException {

  public InvalidTMDbApiKeyException(String message) {
    super(message);
  }

}
