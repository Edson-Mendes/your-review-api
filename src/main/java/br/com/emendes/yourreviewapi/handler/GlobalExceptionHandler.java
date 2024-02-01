package br.com.emendes.yourreviewapi.handler;

import br.com.emendes.yourreviewapi.exception.EmailAlreadyInUseException;
import br.com.emendes.yourreviewapi.exception.MovieNotFoundException;
import br.com.emendes.yourreviewapi.exception.PasswordsDoesNotMatchException;
import br.com.emendes.yourreviewapi.exception.ReviewAlreadyExistsException;
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

    ProblemDetail body = ProblemDetail.forStatus(400);

    body.setTitle("Bad request");
    body.setDetail("Some fields are invalids");
    body.setType(URI.create("https://github.com/Edson-Mendes/your-review-api"));
    body.setProperty("fields", fields);
    body.setProperty("messages", messages);

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(PasswordsDoesNotMatchException.class)
  public ResponseEntity<ProblemDetail> handlePasswordsDoesNotMatch(PasswordsDoesNotMatchException exception) {
    ProblemDetail body = ProblemDetail.forStatus(400);
    body.setTitle("Bad request");
    body.setDetail(exception.getMessage());
    body.setType(URI.create("https://github.com/Edson-Mendes/your-review-api"));

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(EmailAlreadyInUseException.class)
  public ResponseEntity<ProblemDetail> handleEmailAlreadyInUse(EmailAlreadyInUseException exception) {
    ProblemDetail body = ProblemDetail.forStatus(400);
    body.setTitle("Bad request");
    body.setDetail(exception.getMessage());
    body.setType(URI.create("https://github.com/Edson-Mendes/your-review-api"));

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(LockedException.class)
  public ResponseEntity<ProblemDetail> handleBadCredentials(LockedException exception) {
    ProblemDetail body = ProblemDetail.forStatus(400);
    body.setTitle("Bad request");
    body.setDetail(exception.getMessage() + ", check your email for activate your account");
    body.setType(URI.create("https://github.com/Edson-Mendes/your-review-api"));

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ProblemDetail> handleBadCredentials(BadCredentialsException exception) {
    ProblemDetail body = ProblemDetail.forStatus(400);
    body.setTitle(exception.getMessage());
    body.setDetail("wrong email or password");
    body.setType(URI.create("https://github.com/Edson-Mendes/your-review-api"));

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<ProblemDetail> handleBadCredentials(DisabledException exception) {
    ProblemDetail body = ProblemDetail.forStatus(400);
    body.setTitle(exception.getMessage());
    body.setDetail("wrong email or password");
    body.setType(URI.create("https://github.com/Edson-Mendes/your-review-api"));

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(MovieNotFoundException.class)
  public ResponseEntity<ProblemDetail> handleMovieNotFound(MovieNotFoundException exception) {
    ProblemDetail body = ProblemDetail.forStatus(404);
    body.setTitle("Not found");
    body.setDetail(exception.getMessage());
    body.setType(URI.create("https://github.com/Edson-Mendes/your-review-api"));

    return ResponseEntity.status(404).body(body);
  }

  @ExceptionHandler(ReviewAlreadyExistsException.class)
  public ResponseEntity<ProblemDetail> handleReviewAlreadyExists(ReviewAlreadyExistsException exception) {
    ProblemDetail body = ProblemDetail.forStatus(400);
    body.setTitle("Bad request");
    body.setDetail(exception.getMessage());
    body.setType(URI.create("https://github.com/Edson-Mendes/your-review-api"));

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ProblemDetail> handleRuntime(RuntimeException exception) {
    ProblemDetail body = ProblemDetail.forStatus(500);
    body.setTitle("Internal server error");
    body.setDetail(exception.getMessage());
    body.setType(URI.create("https://github.com/Edson-Mendes/your-review-api"));

    return ResponseEntity.internalServerError().body(body);
  }

  @ExceptionHandler(WebClientResponseException.class)
  public ResponseEntity<ProblemDetail> handleWebClientResponse(WebClientResponseException exception) {
    ProblemDetail body = ProblemDetail.forStatus(500);
    body.setTitle("Internal server error");
    String detailsMessageTemplate = "The request %s %s responded with status %d";

    String httpMethod = "*";
    String uri = "**";
    HttpRequest request = exception.getRequest();
    if (request != null) {
      httpMethod = request.getMethod().name();
      uri = request.getURI().getHost() + request.getURI().getPath();
    }

    body.setDetail(detailsMessageTemplate.formatted(httpMethod, uri, exception.getStatusCode().value()));

    body.setType(URI.create("https://github.com/Edson-Mendes/your-review-api"));

    return ResponseEntity.internalServerError().body(body);
  }

}
