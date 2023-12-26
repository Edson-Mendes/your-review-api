package br.com.emendes.yourreviewapi.model.entity;

import br.com.emendes.yourreviewapi.model.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

/**
 * Classe que representa a entidade User do banco de dados.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_user")
public class User implements UserDetails {

  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(name = "name", nullable = false, length = 150)
  private String name;
  @Column(name = "email", nullable = false, length = 320)
  private String email;
  @Column(name = "password", nullable = false, unique = true)
  private String password;
  @Column(name = "status", nullable = false, length = 50)
  @Enumerated(value = EnumType.STRING)
  private Status status;
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "tb_user_authorities",
      joinColumns = {@JoinColumn(name = "user_id", nullable = false)},
      inverseJoinColumns = {@JoinColumn(name = "authority_id", nullable = false)}
  )
  private Collection<Authority> authorities;

  public void addAuthority(Authority authority) {
    if (authorities == null) authorities = new HashSet<>();

    authorities.add(authority);
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return !status.equals(Status.DELETED);
  }

  @Override
  public boolean isAccountNonLocked() {
    return !status.equals(Status.BLOCKED);
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return status.equals(Status.ENABLED);
  }


}
