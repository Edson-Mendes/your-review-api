package br.com.emendes.yourreviewapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;

/**
 * Record que representa um filme do TMDb API.
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
public record TMDbMovieResponse(
    String id,
    String title,
    String overview,
    @JsonProperty("release_date")
    LocalDate releaseDate,
    @JsonProperty("poster_path")
    String posterPath,
    @JsonProperty("backdrop_path")
    String backdropPath,
    @JsonProperty("original_language")
    String originalLanguage
) {
}
