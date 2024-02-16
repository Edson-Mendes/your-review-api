package br.com.emendes.yourreviewapi.util.component.impl;

import br.com.emendes.yourreviewapi.exception.UserIsNotAuthenticatedException;
import br.com.emendes.yourreviewapi.model.entity.User;
import br.com.emendes.yourreviewapi.util.component.AuthenticatedUserComponent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Implementação de {@link AuthenticatedUserComponent}.
 */
@Component
public class AuthenticatedUserComponentImpl implements AuthenticatedUserComponent {

  @Override
  public User getCurrentUser() {
    Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();

    if (currentAuthentication == null ||
        !currentAuthentication.isAuthenticated() ||
        !(currentAuthentication.getPrincipal() instanceof User)) {
      throw new UserIsNotAuthenticatedException("User is not authenticate");
    }

    return (User) currentAuthentication.getPrincipal();
  }

}
