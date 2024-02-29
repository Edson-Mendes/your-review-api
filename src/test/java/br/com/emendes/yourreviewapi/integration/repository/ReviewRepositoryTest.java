package br.com.emendes.yourreviewapi.integration.repository;

import br.com.emendes.yourreviewapi.model.entity.Review;
import br.com.emendes.yourreviewapi.repository.ReviewRepository;
import br.com.emendes.yourreviewapi.repository.projection.ReviewDetailsProjection;
import br.com.emendes.yourreviewapi.repository.projection.ReviewSummaryProjection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.jdbc.SqlMergeMode;

import java.util.List;
import java.util.Optional;

import static br.com.emendes.yourreviewapi.util.path.SqlPath.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("repository-test")
@DisplayName("Tests for ReviewRepository")
@SqlGroup({
    @Sql(scripts = {CREATE_DATABASE_TABLES_SQL_PATH}),
    @Sql(scripts = {DROP_DATABASE_TABLES_SQL_PATH}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class ReviewRepositoryTest {

  @Autowired
  private ReviewRepository reviewRepository;

  @Nested
  @DisplayName("FindProjectedByMovieVotesMovieId Method")
  class findProjectedByMovieVotesMovieIdMethod {

    private static final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 20);

    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(scripts = {INSERT_TWO_REVIEWS_FOR_SAME_MOVIE_SQL_PATH})
    @Test
    @DisplayName("findProjectedByMovieVotesMovieId must return Page<ReviewSummaryProjection> with two elements when found successfully")
    void findProjectedByMovieVotesMovieId_MustReturnPageReviewSummaryProjectionWith2Elements_WhenFoundSuccessfully() {
      Page<ReviewSummaryProjection> actualReviewSummaryProjectionPage = reviewRepository.
          findProjectedByMovieVotesMovieId("120", DEFAULT_PAGEABLE);

      assertThat(actualReviewSummaryProjectionPage).isNotNull().isNotEmpty().hasSize(2);
    }

    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(scripts = {INSERT_REVIEW_SQL_PATH})
    @Test
    @DisplayName("findProjectedByMovieVotesMovieId must return Page<ReviewSummaryProjection> with one element when found successfully")
    void findProjectedByMovieVotesMovieId_MustReturnPageReviewSummaryProjectionWith1Element_WhenFoundSuccessfully() {
      List<ReviewSummaryProjection> actualReviewSummaryProjectionList = reviewRepository.
          findProjectedByMovieVotesMovieId("120", DEFAULT_PAGEABLE).toList();

      assertThat(actualReviewSummaryProjectionList).isNotNull().isNotEmpty().hasSize(1);

      ReviewSummaryProjection actualReviewSummaryProjection = actualReviewSummaryProjectionList.get(0);

      assertThat(actualReviewSummaryProjection).isNotNull();
      assertThat(actualReviewSummaryProjection.getId()).isNotNull().isEqualTo(1L);
      assertThat(actualReviewSummaryProjection.getVote()).isEqualTo(9);
      assertThat(actualReviewSummaryProjection.getOpinion()).isNotNull().isEqualTo("lorem ipsum dolor sit amet");
      assertThat(actualReviewSummaryProjection.getUserId()).isNotNull().isEqualTo(1L);
      assertThat(actualReviewSummaryProjection.getUserName()).isNotNull().isEqualTo("Lorem Ipsum");
      assertThat(actualReviewSummaryProjection.getUserEmail()).isNotNull().isEqualTo("lorem@email.com");
      assertThat(actualReviewSummaryProjection.getMovieVotesMovieId()).isNotNull().isEqualTo("120");
    }

    @Test
    @DisplayName("findProjectedByMovieVotesMovieId must return empty Page<ReviewSummaryProjection> when there is no review for the given movieId")
    void findProjectedByMovieVotesMovieId_MustReturnEmptyPageReviewSummaryProjection_WhenThereIsNoReviewForTheGivenMovieId() {
      Page<ReviewSummaryProjection> actualReviewSummaryProjectionPage = reviewRepository.
          findProjectedByMovieVotesMovieId("120", DEFAULT_PAGEABLE);

      assertThat(actualReviewSummaryProjectionPage).isNotNull().isEmpty();
    }

    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(scripts = {INSERT_REVIEW_SQL_PATH})
    @Test
    @DisplayName("findProjectedByMovieVotesMovieId must return empty Page<ReviewSummaryProjection> when fetch page out of limits")
    void findProjectedByMovieVotesMovieId_MustReturnEmptyPageReviewSummaryProjection_WhenFetchPageOutOfLimits() {
      Page<ReviewSummaryProjection> actualReviewSummaryProjectionPage = reviewRepository.
          findProjectedByMovieVotesMovieId("120", PageRequest.of(1, 20));

      assertThat(actualReviewSummaryProjectionPage).isNotNull().isEmpty();
    }

  }

  @Nested
  @DisplayName("ExistsByUserIdAndMovieId Method")
  class existsByUserIdAndMovieIdMethod {

    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(scripts = {INSERT_REVIEW_SQL_PATH})
    @Test
    @DisplayName("existsByUserIdAndMovieId must return true when exists review for given userId and movieId")
    void existsByUserIdAndMovieId_MustReturnTrue_WhenExistsReviewForGivenUserIdAndMovieId() {
      boolean actualExists = reviewRepository.existsByUserIdAndMovieId(1L, "120");

      assertThat(actualExists).isTrue();
    }

    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(scripts = {INSERT_REVIEW_SQL_PATH})
    @Test
    @DisplayName("existsByUserIdAndMovieId must return false when does not exist review for given userId and movieId")
    void existsByUserIdAndMovieId_MustReturnTrue_WhenDoesNotExistReviewForGivenUserIdAndMovieId() {
      boolean actualExists = reviewRepository.existsByUserIdAndMovieId(2L, "120");

      assertThat(actualExists).isFalse();
    }

  }

  @Nested
  @DisplayName("FindProjectedById Method")
  class findProjectedByIdMethod {

    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(scripts = {INSERT_REVIEW_SQL_PATH})
    @Test
    @DisplayName("findProjectedById must return Optional<ReviewDetailsProjection> when found Review for given reviewId")
    void findProjectedById_MustReturnOptionalReviewDetailsProjection_WhenFoundReviewForGivenReviewId() {
      Optional<ReviewDetailsProjection> actualReviewOptional = reviewRepository.findProjectedById(1L);

      assertThat(actualReviewOptional).isNotEmpty();

      ReviewDetailsProjection actualReviewDetailsProjection = actualReviewOptional.get();
      assertThat(actualReviewDetailsProjection.getId()).isNotNull();
      assertThat(actualReviewDetailsProjection.getOpinion()).isNotNull().isEqualTo("lorem ipsum dolor sit amet");
      assertThat(actualReviewDetailsProjection.getVote()).isEqualTo(9);
      assertThat(actualReviewDetailsProjection.getCreatedAt()).isNotNull().isEqualTo("2024-02-18T12:00:00");
      assertThat(actualReviewDetailsProjection.getUserId()).isNotNull().isEqualTo(1L);
      assertThat(actualReviewDetailsProjection.getMovieVotesMovieId()).isNotNull().isEqualTo("120");
    }

    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(scripts = {INSERT_REVIEW_SQL_PATH})
    @Test
    @DisplayName("findProjectedById must return empty Optional when not found Review for given reviewId")
    void findProjectedById_MustReturnEmptyOptional_WhenNotFoundReviewForGivenReviewId() {
      Optional<ReviewDetailsProjection> actualReviewOptional = reviewRepository.findProjectedById(2L);

      assertThat(actualReviewOptional).isEmpty();
    }

  }

  @Nested
  @DisplayName("FindByIdAndUserId Method")
  class findByIdAndUserIdMethod {

    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(scripts = {INSERT_REVIEW_SQL_PATH})
    @Test
    @DisplayName("findByIdAndUserId must return Optional<Review> when found Review for given reviewId and user")
    void findByIdAndUserId_MustReturnOptionalReview_WhenFoundReviewForGivenReviewIdAndUser() {
      Long userId = 1L;

      Optional<Review> actualReviewOptional = reviewRepository.findByIdAndUserId(1L, userId);

      assertThat(actualReviewOptional).isNotEmpty();

      Review actualReview = actualReviewOptional.get();
      assertThat(actualReview.getId()).isNotNull();
      assertThat(actualReview.getOpinion()).isNotNull().isEqualTo("lorem ipsum dolor sit amet");
      assertThat(actualReview.getVote()).isEqualTo(9);
      assertThat(actualReview.getCreatedAt()).isNotNull().isEqualTo("2024-02-18T12:00:00");
      assertThat(actualReview.getUser()).isNotNull();
      assertThat(actualReview.getUser().getId()).isNotNull().isEqualTo(1L);
      assertThat(actualReview.getMovieVotes()).isNotNull();
      assertThat(actualReview.getMovieVotes().getId()).isNotNull().isEqualTo(1L);
    }

    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(scripts = {INSERT_REVIEW_SQL_PATH})
    @Test
    @DisplayName("findByIdAndUserId must return empty Optional when not found Review for given reviewId and user")
    void findByIdAndUserId_MustReturnEmptyOptional_WhenNotFoundReviewForGivenReviewId() {
      Long userId = 1L;

      Optional<Review> actualReviewOptional = reviewRepository.findByIdAndUserId(2L, userId);

      assertThat(actualReviewOptional).isEmpty();
    }

  }

}
