package com.example.accesscontrol.config;

import com.example.accesscontrol.entity.User;
import com.example.accesscontrol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("123456"))
                    .enabled(true)
                    .build();

            User user = User.builder()
                    .email("user@example.com")
                    .password(passwordEncoder.encode("123456"))
                    .enabled(true)
                    .build();

            userRepository.save(admin);
            userRepository.save(user);

            System.out.println("✅ Inserted test users: admin@example.com / user@example.com (password: 123456)");
        }
    }
}
