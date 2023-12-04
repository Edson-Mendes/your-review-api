package br.com.emendes.yourreviewapi.config.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Classe com beans relacionados a security.
 */
@Configuration
public class SecurityBeans {

  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

}
