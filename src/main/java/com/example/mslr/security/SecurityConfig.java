package com.example.mslr.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // allow H2 console
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                // needed for H2 console to work in browser frames
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                // H2 console uses POSTs, easiest in dev is disable CSRF for it
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                // keep default login for now
                .formLogin(Customizer.withDefaults());

        return http.build();
    }
}

