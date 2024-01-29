package br.com.emendes.yourreviewapi.dto.response;

import lombok.Builder;

import java.time.LocalDate;

/**
 * Record DTO com dados resumidos sobre Movie.
 *
 * @param id          identificador do filme.
 * @param title       título do filme.
 * @param releaseDate data de lançamento do filme.
 * @param posterPath  path do poster do filme.
 */
@Builder
public record MovieSummaryResponse(
    String id,
    String title,
    LocalDate releaseDate,
    String posterPath
) {
}
