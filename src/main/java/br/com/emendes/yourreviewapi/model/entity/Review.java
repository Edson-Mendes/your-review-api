package br.com.emendes.yourreviewapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

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
  @ManyToOne
  private MovieVotes movieVotes;

}
