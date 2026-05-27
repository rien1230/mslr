package com.example.mslr.config;


import com.example.mslr.model.Role;
import com.example.mslr.model.User;
import com.example.mslr.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedEcUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String email = "ec@referendum.gov.sr";
//only can use these email and password to access EC.
            if (userRepository.findByEmail(email).isEmpty()) {
                User ec = new User();
                ec.setEmail(email);
                ec.setFullName("Election Commission");
                ec.setDob(LocalDate.of(1995, 6, 1)); //
                ec.setRole(Role.EC);
                ec.setPasswordHash(passwordEncoder.encode("Shangrilavote&2025@"));
                userRepository.save(ec);
                System.out.println(" Created Seeded EC user: " + email);
            }
        };
    }
}
