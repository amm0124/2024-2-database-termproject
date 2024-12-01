package database.termproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TermprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(TermprojectApplication.class, args);
    }

}
