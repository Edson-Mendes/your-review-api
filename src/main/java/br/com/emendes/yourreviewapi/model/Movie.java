package br.com.emendes.yourreviewapi.model;

import lombok.Builder;

import java.time.LocalDate;

/**
 * Record que representa um filme.
 *
 * @param id               identificador do Movie.
 * @param title            título original do filme (no idioma original).
 * @param overview         sinopse do filme.
 * @param releaseDate      data de lançamento do filme.
 * @param posterPath       path da imagem do poster do filme.
 * @param backdropPath     path da imagem de pano de fundo do filme.
 * @param originalLanguage linguagem original do filme.
 */
@Builder
public record Movie(
    String id,
    String title,
    String overview,
    LocalDate releaseDate,
    String posterPath,
    String backdropPath,
    String originalLanguage
) {
}
