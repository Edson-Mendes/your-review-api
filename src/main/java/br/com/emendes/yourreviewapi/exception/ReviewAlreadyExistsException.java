package br.com.emendes.yourreviewapi.exception;

/**
 * Exeception a ser lançada caso um User já tenha feito uma review para um dado filme.
 */
public class ReviewAlreadyExistsException extends RuntimeException {

  public ReviewAlreadyExistsException(String message) {
    super(message);
  }

}
