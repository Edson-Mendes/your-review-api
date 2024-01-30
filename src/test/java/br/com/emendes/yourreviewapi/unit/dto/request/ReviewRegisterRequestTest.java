package br.com.emendes.yourreviewapi.unit.dto.request;

import br.com.emendes.yourreviewapi.config.ValidatorFactoryConfig;
import br.com.emendes.yourreviewapi.dto.request.ReviewRegisterRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ValidatorFactoryConfig.class})
@DisplayName("Unit tests das validações do DTO ReviewRegisterRequest")
class ReviewRegisterRequestTest {

  @Autowired
  private Validator validator;

  @Nested
  @DisplayName("vote validation")
  class VoteValidation {

    private static final String VOTE_PROPERTY = "vote";

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    @DisplayName("Vote validation must not return violations when vote is valid")
    void voteValidation_MustNotReturnViolations_WhenVoteIsValid(int validVote) {
      assumeThat(validVote).isLessThanOrEqualTo(10);
      assumeThat(validVote).isGreaterThanOrEqualTo(1);

      ReviewRegisterRequest reviewRegisterRequest = ReviewRegisterRequest.builder()
          .vote(validVote)
          .build();

      Set<ConstraintViolation<ReviewRegisterRequest>> actualViolations = validator
          .validateProperty(reviewRegisterRequest, VOTE_PROPERTY);

      assertThat(actualViolations).isEmpty();
    }

    @Test
    @DisplayName("Vote validation must return violations when vote is zero")
    void voteValidation_MustReturnViolations_WheVoteIsZero() {
      ReviewRegisterRequest reviewRegisterRequest = ReviewRegisterRequest.builder()
          .vote(0)
          .build();

      Set<ConstraintViolation<ReviewRegisterRequest>> actualViolations = validator
          .validateProperty(reviewRegisterRequest, VOTE_PROPERTY);
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      assertThat(actualViolations).isNotEmpty();
      assertThat(actualMessages).contains("vote must be greater than or equal to 1");
    }

    @Test
    @DisplayName("Vote validation must return violations when vote is 11")
    void voteValidation_MustReturnViolations_WheVoteIs11() {
      ReviewRegisterRequest reviewRegisterRequest = ReviewRegisterRequest.builder()
          .vote(11)
          .build();

      Set<ConstraintViolation<ReviewRegisterRequest>> actualViolations = validator
          .validateProperty(reviewRegisterRequest, VOTE_PROPERTY);
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      assertThat(actualViolations).isNotEmpty();
      assertThat(actualMessages).contains("vote must be less than or equal to 10");
    }

  }

  @Nested
  @DisplayName("opinion validation")
  class OpinionValidation {

    private static final String OPINION_PROPERTY = "opinion";

    @ParameterizedTest
    @ValueSource(strings = {"Lorem ipsum",
        "Lorem ipsum dolor sit ametLorem ipsum dolor sit ametLorem ipsum dolor sit amet" +
            "Lorem ipsum dolor sit ametLorem ipsum dolor sit ametLorem ipsum dolor sit amet" +
            "Lorem ipsum dolor sit ametLorem ipsum dolor sit ametLorem ipsum dolor sit amet" +
            "Lorem ipsum dolor sit ametLorem ipsum dolor sit ametLorem ipsum dolor sit amet" +
            "Lorem ipsum dolor sit ametLorem ipsum dolor sit ametLorem ipsum dolor sit amet" +
            "Lorem ipsum dolor sit ametLorem ipsum dolor sit ametLorem ipsum dolor sit amet" +
            "Lorem ipsum dolor sit ametLorem."})
    @DisplayName("Opinion validation must not return violations when opinion is valid")
    void opinionValidation_MustNotReturnViolations_WhenOpinionIsValid(String validOpinion) {
      assumeThat(validOpinion.length()).isLessThanOrEqualTo(500);

      ReviewRegisterRequest reviewRegisterRequest = ReviewRegisterRequest.builder()
          .opinion(validOpinion)
          .build();

      Set<ConstraintViolation<ReviewRegisterRequest>> actualViolations = validator
          .validateProperty(reviewRegisterRequest, OPINION_PROPERTY);

      assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Opinion validation must not return violations when opinion is null")
    void opinionValidation_MustNotReturnViolations_WhenOpinionIsNull(String nullOpinion) {
      ReviewRegisterRequest reviewRegisterRequest = ReviewRegisterRequest.builder()
          .opinion(nullOpinion)
          .build();

      Set<ConstraintViolation<ReviewRegisterRequest>> actualViolations = validator
          .validateProperty(reviewRegisterRequest, OPINION_PROPERTY);

      assertThat(actualViolations).isEmpty();
    }

    @Test
    @DisplayName("opinion validation must return violation when opinion length is greater than 500")
    void opinionValidation_MustReturnViolations_WhenOpinionLengthIsGreaterThan500() {
      String opinionGreaterThan500 = "Lorem ipsum dolor sit ametLorem ipsum dolor sit ametLorem ipsum dolor sit amet" +
          "Lorem ipsum dolor sit ametLorem ipsum dolor sit ametLorem ipsum dolor sit amet" +
          "Lorem ipsum dolor sit ametLorem ipsum dolor sit ametLorem ipsum dolor sit amet" +
          "Lorem ipsum dolor sit ametLorem ipsum dolor sit ametLorem ipsum dolor sit amet" +
          "Lorem ipsum dolor sit ametLorem ipsum dolor sit ametLorem ipsum dolor sit amet" +
          "Lorem ipsum dolor sit ametLorem ipsum dolor sit ametLorem ipsum dolor sit amet" +
          "Lorem ipsum dolor sit ametLorem..";

      assumeThat(opinionGreaterThan500.length()).isGreaterThan(500);

      ReviewRegisterRequest reviewRegisterRequest = ReviewRegisterRequest.builder()
          .opinion(opinionGreaterThan500)
          .build();

      Set<ConstraintViolation<ReviewRegisterRequest>> actualViolations = validator
          .validateProperty(reviewRegisterRequest, OPINION_PROPERTY);
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      assertThat(actualViolations).isNotEmpty();
      assertThat(actualMessages).contains("opinion size must be less than or equal to 500");
    }

    @ParameterizedTest
    @ValueSource(strings = {"   ", "\t", "\n", ""})
    @DisplayName("opinion validation must return violation when opinion blank")
    void opinionValidation_MustReturnViolations_WhenOpinionBlank(String blankOpinion) {
      ReviewRegisterRequest reviewRegisterRequest = ReviewRegisterRequest.builder()
          .opinion(blankOpinion)
          .build();

      Set<ConstraintViolation<ReviewRegisterRequest>> actualViolations = validator
          .validateProperty(reviewRegisterRequest, OPINION_PROPERTY);
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      assertThat(actualViolations).isNotEmpty();
      assertThat(actualMessages).contains("opinion must not be empty or blank");
    }

  }

  @Nested
  @DisplayName("movieId validation")
  class MovieIdValidation {

    private static final String MOVIE_ID_PROPERTY = "movieId";

    @Test
    @DisplayName("MovieId validation must not return violations when movieId is valid")
    void movieIdValidation_MustNotReturnViolations_WhenMovieIdIsValid() {
      ReviewRegisterRequest reviewRegisterRequest = ReviewRegisterRequest.builder()
          .movieId("120")
          .build();

      Set<ConstraintViolation<ReviewRegisterRequest>> actualViolations = validator
          .validateProperty(reviewRegisterRequest, MOVIE_ID_PROPERTY);

      assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("movieId validation must return violation when movieId blank")
    void movieIdValidation_MustReturnViolations_WhenMovieIdBlank(String blankMovieId) {
      ReviewRegisterRequest reviewRegisterRequest = ReviewRegisterRequest.builder()
          .movieId(blankMovieId)
          .build();

      Set<ConstraintViolation<ReviewRegisterRequest>> actualViolations = validator
          .validateProperty(reviewRegisterRequest, MOVIE_ID_PROPERTY);
      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      assertThat(actualViolations).isNotEmpty();
      assertThat(actualMessages).contains("movieId must not be blank");
    }

  }

}