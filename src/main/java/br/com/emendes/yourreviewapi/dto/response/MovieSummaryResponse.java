package br.com.emendes.yourreviewapi.dto.response;

import lombok.Builder;

import java.net.URI;
import java.time.LocalDate;

/**
 * Record DTO com dados resumidos sobre Movie.
 */
@Builder
public record MovieSummaryResponse(
    String id,
    String originalTitle,
    LocalDate releaseDate,
    String posterPath
) {
}
