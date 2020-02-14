package by.pochepko.hes.testapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("by.pochepko.hes.testapp.repository")
@EntityScan("by.pochepko.hes.testapp.model")
public class TestappApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestappApplication.class, args);
    }

}
