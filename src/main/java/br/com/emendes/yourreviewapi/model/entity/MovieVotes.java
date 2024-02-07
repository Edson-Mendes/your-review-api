package br.com.emendes.yourreviewapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Classe que representa a entidade MovieVotes do banco de dados.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_movie_votes")
public class MovieVotes {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "movie_id", length = 100, nullable = false, unique = true)
  private String movieId;
  @Column(name = "vote_total", nullable = false)
  private long voteTotal;
  @Column(name = "vote_count", nullable = false)
  private long voteCount;
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

}
