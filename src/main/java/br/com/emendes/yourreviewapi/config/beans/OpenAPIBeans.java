package br.com.emendes.yourreviewapi.config.beans;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe com as configurações relacionadas a OpenAPI, Swagger e Springdocs.
 */
@Configuration
public class OpenAPIBeans {

  public static final String SECURITY_SCHEME_KEY = "bearer-key";

  @Bean
  public OpenAPI openAPI() {
    Contact contact = new Contact();
    contact.name("Edson Mendes").email("edson.luiz.mendes@hotmail.com").url("https://github.com/Edson-Mendes");

    SecurityScheme securityScheme = new SecurityScheme()
        .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT");

    return new OpenAPI()
        .info(new Info().title("Your Review API")
            .description("System where the user can give their opinion and vote about movies, and read reviews/opinions from other users about the movies.")
            .version("v1.0.0")
            .contact(contact))
        .components(new Components().addSecuritySchemes(SECURITY_SCHEME_KEY, securityScheme));
  }

}
