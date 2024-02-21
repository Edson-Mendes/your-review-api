package br.com.emendes.yourreviewapi.service.impl;

import br.com.emendes.yourreviewapi.dto.request.ReviewRegisterRequest;
import br.com.emendes.yourreviewapi.dto.request.ReviewUpdateRequest;
import br.com.emendes.yourreviewapi.dto.response.ReviewDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewSummaryResponse;
import br.com.emendes.yourreviewapi.exception.ReviewAlreadyExistsException;
import br.com.emendes.yourreviewapi.exception.ReviewNotFoundException;
import br.com.emendes.yourreviewapi.mapper.ReviewMapper;
import br.com.emendes.yourreviewapi.model.entity.MovieVotes;
import br.com.emendes.yourreviewapi.model.entity.Review;
import br.com.emendes.yourreviewapi.model.entity.User;
import br.com.emendes.yourreviewapi.repository.ReviewRepository;
import br.com.emendes.yourreviewapi.service.MovieVotesService;
import br.com.emendes.yourreviewapi.service.ReviewService;
import br.com.emendes.yourreviewapi.util.component.AuthenticatedUserComponent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Implementação de {@link ReviewService}.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

  private final AuthenticatedUserComponent authenticatedUserComponent;
  private final MovieVotesService movieVotesService;
  private final ReviewMapper reviewMapper;
  private final ReviewRepository reviewRepository;

  @Transactional
  @Override
  public ReviewDetailsResponse register(ReviewRegisterRequest reviewRegisterRequest) {
    log.info("Attempt to register a review for movie with id: {}", reviewRegisterRequest.movieId());

    User currentUser = authenticatedUserComponent.getCurrentUser();
    if (reviewRepository.existsByUserIdAndMovieId(currentUser.getId(), reviewRegisterRequest.movieId())) {
      String message = "User %s has already reviewed the movie with id %s"
          .formatted(currentUser.getEmail(), reviewRegisterRequest.movieId());

      log.info(message);
      throw new ReviewAlreadyExistsException(message);
    }

    MovieVotes movieVotes = movieVotesService.findByMovieId(reviewRegisterRequest.movieId())
        .orElseGet(() -> movieVotesService.generateNonVotedMovieVotes(reviewRegisterRequest.movieId()));

    Review review = reviewMapper.toReview(reviewRegisterRequest);
    review.setUser(currentUser);
    review.setMovieVotes(movieVotes);
    review.setCreatedAt(LocalDateTime.now());

    movieVotes.setVoteTotal(movieVotes.getVoteTotal() + review.getVote());
    movieVotes.setVoteCount(movieVotes.getVoteCount() + 1);

    review = reviewRepository.save(review);

    log.info("Review registered successfully with id: {}", review.getId());
    return reviewMapper.toReviewDetailsResponse(review);
  }

  @Override
  public Page<ReviewSummaryResponse> fetchByMovieId(String movieId, int page) {
    log.info("Attempt to fetch review for movie with id: {}", movieId);

    Pageable pageable = PageRequest.of(page, 20);
    return reviewRepository.findProjectedByMovieVotesMovieId(movieId, pageable)
        .map(reviewMapper::toReviewSummaryResponse);
  }

  @Override
  public ReviewDetailsResponse findById(Long reviewId) {
    log.info("Attempt to find review with id: {}", reviewId);

    return reviewRepository.findProjectedById(reviewId)
        .map(reviewMapper::toReviewDetailsResponse)
        .orElseThrow(() -> getReviewNotFoundException(reviewId));
  }

  @Override
  public ReviewDetailsResponse updateById(Long reviewId, ReviewUpdateRequest reviewUpdateRequest) {
    log.info("Attempt to update review with id: {}", reviewId);
    User currentUser = authenticatedUserComponent.getCurrentUser();

    Review review = reviewRepository.findByIdAndUser(reviewId, currentUser)
        .orElseThrow(() -> getReviewNotFoundException(reviewId));

    updateVoteTotal(review.getMovieVotes(), review.getVote(), reviewUpdateRequest.vote());

    reviewMapper.merge(review, reviewUpdateRequest);
    review = reviewRepository.save(review);

    log.info("Review with id: {} updated successfully", reviewId);

    return reviewMapper.toReviewDetailsResponse(review);
  }

  @Transactional
  @Override
  public void deleteById(Long reviewId) {
    log.info("Attempt to delete review with id: {}", reviewId);
    User currentUser = authenticatedUserComponent.getCurrentUser();

    Optional<Review> reviewOptional = reviewRepository.findByIdAndUser(reviewId, currentUser);
    if (reviewOptional.isEmpty()) {
      throw getReviewNotFoundException(reviewId);
    }
    Review review = reviewOptional.get();

    MovieVotes movieVotes = review.getMovieVotes();
    movieVotes.setVoteCount(movieVotes.getVoteCount() - 1);
    movieVotes.setVoteTotal(movieVotes.getVoteTotal() - review.getVote());

    reviewRepository.delete(review);
    log.info("Review with id: {} deleted successfully", reviewId);
  }

  /**
   * Atualiza {@link MovieVotes#getVoteTotal} de acordo com o valor antigo de {@link Review#getVote()}
   * e o novo valor do voto da review.
   *
   * @param movieVotes Objeto MovieVotes que terá seu campo MovieVotes.voteTotal atualizado.
   * @param oldVote    antigo valor do vote da Review.
   * @param newVote    novo valor do vote da Review.
   */
  private static void updateVoteTotal(MovieVotes movieVotes, long oldVote, long newVote) {
    movieVotes.setVoteTotal(movieVotes.getVoteTotal() - (oldVote - newVote));
  }

  /**
   * Retorna {@link ReviewNotFoundException} para o dado {@code reviewId}.
   *
   * @param reviewId identificador a ser mostrado na mensagem da exception.
   * @return {@link ReviewNotFoundException} com a mensage para o dado {@code reviewId}.
   */
  private static ReviewNotFoundException getReviewNotFoundException(Long reviewId) {
    String message = "Review not found for id: %d".formatted(reviewId);
    log.info(message);

    return new ReviewNotFoundException(message);
  }

}
