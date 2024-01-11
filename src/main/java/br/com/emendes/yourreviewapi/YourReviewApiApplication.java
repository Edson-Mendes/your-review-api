package br.com.emendes.yourreviewapi;

import br.com.emendes.yourreviewapi.util.properties.JwtProperties;
import br.com.emendes.yourreviewapi.util.properties.TMDbPathProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class, TMDbPathProperties.class})
@EnableCaching
public class YourReviewApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(YourReviewApiApplication.class, args);
  }

}
