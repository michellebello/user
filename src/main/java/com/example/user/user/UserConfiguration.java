package com.example.user.user;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserConfiguration {
    @Bean
    CommandLineRunner commandLineRunner(UserRepository repository){
        return args -> {
            User michelle = new User(
                    "mich",
                    "12345",
                    "mich@gmail.com"
            );
            repository.saveAll(List.of(michelle));
        };
    }

}
