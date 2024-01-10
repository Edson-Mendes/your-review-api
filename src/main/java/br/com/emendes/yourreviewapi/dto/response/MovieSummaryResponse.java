package br.com.emendes.yourreviewapi.dto.response;

import lombok.Builder;

import java.time.LocalDate;

/**
 * Record DTO com dados resumidos sobre Movie.
 */
@Builder
public record MovieSummaryResponse(
    String id,
    String title,
    LocalDate releaseDate,
    String posterPath
) {
}
