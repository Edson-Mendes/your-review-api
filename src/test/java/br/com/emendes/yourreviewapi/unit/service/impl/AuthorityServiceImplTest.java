package br.com.emendes.yourreviewapi.unit.service.impl;

import br.com.emendes.yourreviewapi.exception.AuthorityNotFoundException;
import br.com.emendes.yourreviewapi.model.entity.Authority;
import br.com.emendes.yourreviewapi.repository.AuthorityRepository;
import br.com.emendes.yourreviewapi.service.impl.AuthorityServiceImpl;
import br.com.emendes.yourreviewapi.util.faker.AuthorityFaker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests de AuthorityServiceImpl")
class AuthorityServiceImplTest {

  @InjectMocks
  private AuthorityServiceImpl authorityService;

  @Mock
  private AuthorityRepository authorityRepositoryMock;

  @Nested
  @DisplayName("findByName method")
  class FindByNameMethod {

    @Test
    @DisplayName("findByName must return Authority when found successfully")
    void findByName_MustReturnAuthority_WhenFoundSuccessfully() {
      when(authorityRepositoryMock.findByName("USER"))
          .thenReturn(Optional.of(AuthorityFaker.userAuthority()));

      Authority actualAuthority = authorityService.findByName("USER");

      assertThat(actualAuthority).isNotNull();
      assertThat(actualAuthority.getId()).isNotNull();
      assertThat(actualAuthority.getName()).isNotNull().isEqualTo("USER");
    }

    @Test
    @DisplayName("findByName must throw IllegalArgumentException when name is null")
    void findByName_MustThrowIllegalArgumentException_WhenName() {
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> authorityService.findByName(null))
          .withMessage("name must not be null");
    }

    @Test
    @DisplayName("findByName must throw AuthorityNotFoundException when not found Authority")
    void findByName_MustThrowAuthorityNotFoundException_WhenNotFoundAuthority() {
      assertThatExceptionOfType(AuthorityNotFoundException.class)
          .isThrownBy(() -> authorityService.findByName("LOREM"))
          .withMessage("Authority not found for name: LOREM");
    }

  }

}