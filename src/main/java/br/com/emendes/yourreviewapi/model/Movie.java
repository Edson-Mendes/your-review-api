package br.com.emendes.yourreviewapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

/**
 * Record que representa um filme.
 *
 * @param id               identificador do Movie.
 * @param originalTitle    título original do filme (no idioma original).
 * @param overview         sinopse do filme.
 * @param releaseDate      data de lançamento do filme.
 * @param posterPath       URI da imagem do poster do filme.
 * @param originalLanguage linguagem original do filme.
 */
public record Movie(
    String id,
    String title,
    String overview,
    @JsonProperty("release_date")
    LocalDate releaseDate,
    @JsonProperty("poster_path")
    String posterPath,
    @JsonProperty("original_language")
    String originalLanguage
) {
}
