package br.com.emendes.yourreviewapi.service.impl;

import br.com.emendes.yourreviewapi.dto.request.ReviewRegisterRequest;
import br.com.emendes.yourreviewapi.dto.response.ReviewDetailsResponse;
import br.com.emendes.yourreviewapi.exception.ReviewAlreadyExistsException;
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
import org.springframework.stereotype.Service;

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
    MovieVotes movieVotes = movieVotesService.findByMovieId(reviewRegisterRequest.movieId());

    if (reviewRepository.existsByUserAndMovieVotes(currentUser, movieVotes)) {
      String message = "User %s has already reviewed the movie with id %s"
          .formatted(currentUser.getEmail(), movieVotes.getMovieId());

      log.info(message);
      throw new ReviewAlreadyExistsException(message);
    }

    Review review = reviewMapper.toReview(reviewRegisterRequest);
    review.setUser(currentUser);
    review.setMovieVotes(movieVotes);

    review = reviewRepository.save(review);
    movieVotesService.updateById(review.getMovieVotes().getId(), review.getVote());

    log.info("Review registered successfully with id: {}", review.getId());
    return reviewMapper.toReviewDetailsResponse(review);
  }

}
