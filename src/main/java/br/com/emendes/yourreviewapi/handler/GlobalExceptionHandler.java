package br.com.emendes.yourreviewapi.handler;

import br.com.emendes.yourreviewapi.exception.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.stream.Collectors;

/**
 * Classe responsável por lidar com algumas exceptions que ocorrerem a partir da controller.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  public static final String BAD_REQUEST_TITLE = "Bad request";
  public static final String INTERNAL_SERVER_ERROR_TITLE = "Internal server error";
  public static final String PROBLEM_DETAIL_URI = "https://github.com/Edson-Mendes/your-review-api";

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ProblemDetail> handleConstraintViolation(ConstraintViolationException exception) {
    String messages = exception.getConstraintViolations()
        .stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.joining(";"));
    String fields = exception.getConstraintViolations()
        .stream()
        .map(constraintViolation -> {
          String propertyPath = constraintViolation.getPropertyPath().toString();
          String[] propertyPathSplit = propertyPath.split("\\.");
          int lastIndex = propertyPathSplit.length - 1;
          return propertyPathSplit[lastIndex];
        })
        .collect(Collectors.joining(";"));

    ProblemDetail body = createProblemDetail(BAD_REQUEST_TITLE, "Some fields are invalids", 400);
    body.setProperty("fields", fields);
    body.setProperty("messages", messages);

    return createResponseEntity(body);
  }

  @ExceptionHandler(PasswordsDoesNotMatchException.class)
  public ResponseEntity<ProblemDetail> handlePasswordsDoesNotMatch(PasswordsDoesNotMatchException exception) {
    return createResponseEntity(createProblemDetail(BAD_REQUEST_TITLE, exception.getMessage(), 400));
  }

  @ExceptionHandler(EmailAlreadyInUseException.class)
  public ResponseEntity<ProblemDetail> handleEmailAlreadyInUse(EmailAlreadyInUseException exception) {
    return createResponseEntity(createProblemDetail(BAD_REQUEST_TITLE, exception.getMessage(), 400));
  }

  @ExceptionHandler(LockedException.class)
  public ResponseEntity<ProblemDetail> handleLocked(LockedException exception) {
    return createResponseEntity(createProblemDetail(
        BAD_REQUEST_TITLE,
        exception.getMessage() + ", check your email for activate your account",
        400));
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ProblemDetail> handleBadCredentials(BadCredentialsException exception) {
    return createResponseEntity(createProblemDetail(
        exception.getMessage(),
        "wrong email or password",
        400));
  }

  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<ProblemDetail> handleDisabled(DisabledException exception) {
    return createResponseEntity(createProblemDetail(
        exception.getMessage(),
        "this account has been disabled",
        400));
  }

  @ExceptionHandler(MovieNotFoundException.class)
  public ResponseEntity<ProblemDetail> handleMovieNotFound(MovieNotFoundException exception) {
    return createResponseEntity(createProblemDetail(
        "Movie not found",
        exception.getMessage(),
        exception.getStatusCode()));
  }

  @ExceptionHandler(ReviewNotFoundException.class)
  public ResponseEntity<ProblemDetail> handleReviewNotFound(ReviewNotFoundException exception) {
    return createResponseEntity(createProblemDetail("Review not found", exception.getMessage(), 404));
  }

  @ExceptionHandler(ReviewAlreadyExistsException.class)
  public ResponseEntity<ProblemDetail> handleReviewAlreadyExists(ReviewAlreadyExistsException exception) {
    return createResponseEntity(createProblemDetail(BAD_REQUEST_TITLE, exception.getMessage(), 400));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ProblemDetail> handleRuntime(RuntimeException exception) {
    log.error(INTERNAL_SERVER_ERROR_TITLE, exception);

    return createResponseEntity(createProblemDetail(INTERNAL_SERVER_ERROR_TITLE, exception.getMessage(), 500));
  }

  @ExceptionHandler(WebClientResponseException.class)
  public ResponseEntity<ProblemDetail> handleWebClientResponse(WebClientResponseException exception) {
    String detailsMessageTemplate = "The request %s %s responded with status %d";
    String httpMethod = "*";
    String uri = "**";
    HttpRequest request = exception.getRequest();

    if (request != null) {
      httpMethod = request.getMethod().name();
      uri = request.getURI().getHost() + request.getURI().getPath();
    }

    return createResponseEntity(createProblemDetail(
        INTERNAL_SERVER_ERROR_TITLE,
        detailsMessageTemplate.formatted(httpMethod, uri, exception.getStatusCode().value()),
        500));
  }

  /**
   * Gera uma instância de {@link ProblemDetail} a partir dos parâmetros {@code title}, {@code detail} e
   * {@code statusCode}, o campo type é padrão {@code https://github.com/Edson-Mendes/your-review-api}.
   *
   * @param title      campo {@code title} do corpo da resposta.
   * @param detail     campo {@code title} do corpo da resposta.
   * @param statusCode campo {@code status} do corpo da resposta.
   * @return objeto ProblemDetail
   */
  private ProblemDetail createProblemDetail(String title, String detail, int statusCode) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(statusCode);
    problemDetail.setTitle(title);
    problemDetail.setDetail(detail);
    problemDetail.setType(URI.create(PROBLEM_DETAIL_URI));

    return problemDetail;
  }

  /**
   * Cria uma instância de {@link ResponseEntity} a partir de um objeto {@link ProblemDetail} que representa o
   * body da resposta, o status da resposta corresponde ao campo status do ProblemDetail.
   *
   * @param body objeto que será o corpo da resposta.
   * @return {@code ResponseEntity<ProblemDetail>}
   */
  private ResponseEntity<ProblemDetail> createResponseEntity(ProblemDetail body) {
    return ResponseEntity.status(body.getStatus()).body(body);
  }

}
