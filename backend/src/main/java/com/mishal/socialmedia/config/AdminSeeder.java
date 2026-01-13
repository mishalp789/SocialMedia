package com.mishal.socialmedia.config;

import com.mishal.socialmedia.entity.User;
import com.mishal.socialmedia.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeeder {

    @Bean
    CommandLineRunner createAdmin(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder) {

        return args -> {
            userRepository.findByUsername("admin")
                    .orElseGet(() -> {
                        User admin = new User();
                        admin.setUsername("admin");
                        admin.setEmail("admin@social.com");
                        admin.setPassword(passwordEncoder.encode("admin123"));
                        admin.setRole("ROLE_ADMIN");
                        return userRepository.save(admin);
                    });
        };
    }
}
