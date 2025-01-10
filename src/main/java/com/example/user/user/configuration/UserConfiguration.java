package com.example.user.user.configuration;

import com.example.user.user.repository.UserRepository;
import com.example.user.user.user.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

@Configuration
public class UserConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            String password = "12345";
            String hashedPassword = passwordEncoder.encode(password); // Hash the password

            User michelle = new User(
                    "mich",
                    hashedPassword,
                    "mich@gmail.com"
            );
            repository.saveAll(List.of(michelle));  // Save the user to the database
        };
    }
}