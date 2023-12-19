package br.com.emendes.yourreviewapi;

import br.com.emendes.yourreviewapi.util.properties.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class})
public class YourReviewApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(YourReviewApiApplication.class, args);
  }

}
