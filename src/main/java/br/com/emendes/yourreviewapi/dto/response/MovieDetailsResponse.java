package br.com.emendes.yourreviewapi.dto.response;

import lombok.Builder;

import java.time.LocalDate;

/**
 * Record DTO com dados detalhados sobre Movie.
 *
 * @param id               identificador do filme.
 * @param title            título do filme.
 * @param overview         sinopse do filme.
 * @param releaseDate      data de lançamento do filme.
 * @param originalLanguage idioma original do filme.
 * @param posterPath       path da imagem do poster do filme.
 * @param backdropPath     path da imagem de pano de fundo do filme.
 */
@Builder
public record MovieDetailsResponse(
    String id,
    String title,
    String overview,
    LocalDate releaseDate,
    String originalLanguage,
    String posterPath,
    String backdropPath
) {
}
