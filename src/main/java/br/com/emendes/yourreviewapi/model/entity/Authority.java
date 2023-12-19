package br.com.emendes.yourreviewapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

/**
 * Classe que representa a entidade Authority do banco de dados.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_authority")
public class Authority implements GrantedAuthority {

  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;
  @Column(name = "name", nullable = false, length = 150, unique = true)
  private String name;

  @Override
  public String getAuthority() {
    return name;
  }
}
