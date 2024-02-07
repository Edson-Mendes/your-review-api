package br.com.emendes.yourreviewapi.service.impl;

import br.com.emendes.yourreviewapi.dto.request.ReviewRegisterRequest;
import br.com.emendes.yourreviewapi.dto.response.ReviewDetailsResponse;
import br.com.emendes.yourreviewapi.dto.response.ReviewSummaryResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

    review = reviewRepository.save(review);

    log.info("Review registered successfully with id: {}", review.getId());
    return reviewMapper.toReviewDetailsResponse(review);
  }

  @Override
  public Page<ReviewSummaryResponse> fetchByMovieId(String movieId, int page) {
    log.info("Attempt to fetch review for movie with id: {}", movieId);

    Pageable pageable = PageRequest.of(page, 20);
    Optional<MovieVotes> movieVotesOptional = movieVotesService.findByMovieId(movieId);

    if (movieVotesOptional.isEmpty()) {
      return new PageImpl<>(List.of(), pageable, 0);
    }

    Page<Review> reviewPage = reviewRepository.findByMovieVotes(movieVotesOptional.get(), pageable);
    return reviewPage.map(reviewMapper::toReviewSummaryResponse);
  }

}
