package app;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan//({"app.services", "app.controllers"})
@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories
@SpringBootApplication
public class Application {
    @SneakyThrows
    public static void main(String[] args) {
        // Giving the DB some time to get out of bed
        Thread.sleep(8000);

        SpringApplication.run(Application.class, args);
    }
}

