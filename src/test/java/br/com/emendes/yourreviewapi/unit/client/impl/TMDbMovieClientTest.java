package br.com.emendes.yourreviewapi.unit.client.impl;

import br.com.emendes.yourreviewapi.client.impl.TMDbMovieClient;
import br.com.emendes.yourreviewapi.dto.response.TMDbMovieResponse;
import br.com.emendes.yourreviewapi.dto.response.TMDbSearchMovieResponse;
import br.com.emendes.yourreviewapi.mapper.MovieMapper;
import br.com.emendes.yourreviewapi.model.Movie;
import br.com.emendes.yourreviewapi.util.faker.MovieFaker;
import br.com.emendes.yourreviewapi.util.faker.TMDbFaker;
import br.com.emendes.yourreviewapi.util.properties.TMDbPathProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for TMDbMovieClient")
class TMDbMovieClientTest {

  @InjectMocks
  private TMDbMovieClient movieClient;
  @Mock
  private WebClient webClientMock;
  @Mock
  private TMDbPathProperties tmDbPathPropertiesMock;
  @Mock
  private MovieMapper movieMapperMock;

  @Nested
  @DisplayName("Tests for findByName method")
  class FindByNameMethod {

    private static final int VALID_PAGE = 1;
    private static final String VALID_NAME = "lorem";

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;
    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpecMock;
    @Mock
    private WebClient.ResponseSpec responseSpecMock;

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("findByName must return Page<Movie> when found successfully")
    void findByName_MustReturnPageMovie_WhenFoundSuccessfully() {
      TMDbSearchMovieResponse response = TMDbSearchMovieResponse.builder()
          .page(1)
          .results(List.of(TMDbFaker.tMDbMovieResponse()))
          .totalResults(1)
          .totalPages(1)
          .build();

      when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
      when(requestHeadersUriSpecMock.uri(any(Function.class))).thenReturn(requestHeadersSpecMock);
      when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
      when(responseSpecMock.onStatus(any(), any())).thenReturn(responseSpecMock);
      when(responseSpecMock.bodyToMono(ArgumentMatchers.<Class<TMDbSearchMovieResponse>>notNull()))
          .thenReturn(Mono.just(response));
      when(movieMapperMock.toMovie(any(TMDbMovieResponse.class))).thenReturn(MovieFaker.movie());

      Page<Movie> actualMoviePage = movieClient.findByName("Lorem", 1);

      assertThat(actualMoviePage).isNotNull().hasSize(1);
      assertThat(actualMoviePage.stream().toList()).contains(MovieFaker.movie());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("findByName must throw IllegalArgumentException when name is invalid")
    void findByName_MustThrowIllegalArgumentException_WhenNameIsNull(String invalidName) {
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> movieClient.findByName(invalidName, VALID_PAGE))
          .withMessage("name must not be null or blank");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -2})
    @DisplayName("findByName must throw IllegalArgumentException when page is less than one")
    void findByName_MustThrowIllegalArgumentException_WhenPageIsLessThanOne(int negativePage) {
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> movieClient.findByName(VALID_NAME, negativePage))
          .withMessage("page must not be negative");
    }

  }

  @Nested
  @DisplayName("Tests for findById method")
  class FindByIdMethod {

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;
    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpecMock;
    @Mock
    private WebClient.ResponseSpec responseSpecMock;

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("findById must return Movie when found movie with id 1000000 successfully")
    void findById_MustReturnMovie_WhenFoundMovieWithId1000000Successfully() {
      when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
      when(requestHeadersUriSpecMock.uri(any(Function.class))).thenReturn(requestHeadersSpecMock);
      when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
      when(responseSpecMock.onStatus(any(), any())).thenReturn(responseSpecMock);
      when(responseSpecMock.bodyToMono(ArgumentMatchers.<Class<TMDbMovieResponse>>notNull()))
          .thenReturn(Mono.just(TMDbFaker.tMDbMovieResponse()));
      when(movieMapperMock.toMovie(any(TMDbMovieResponse.class))).thenReturn(MovieFaker.movie());

      Movie actualMovie = movieClient.findById("1000000");

      assertThat(actualMovie).isNotNull().isEqualTo(MovieFaker.movie());
    }

  }

}