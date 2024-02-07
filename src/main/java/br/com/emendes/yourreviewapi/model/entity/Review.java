package br.com.emendes.yourreviewapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Classe que representa a entidade Review do banco de dados.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_review")
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "vote", nullable = false)
  private int vote;
  @Column(name = "opinion")
  private String opinion;
  @ManyToOne
  private User user;
  @ManyToOne(cascade = {CascadeType.PERSIST})
  private MovieVotes movieVotes;
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

}
