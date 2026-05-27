package com.example.mslr.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(
            DbUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            DaoAuthenticationProvider provider
    ) throws Exception {

        http.authenticationProvider(provider);

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/error").permitAll()
                .requestMatchers("/users/register", "/users/register/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                .requestMatchers("/ec/**").hasRole("EC")
                .requestMatchers("/voter/**").hasRole("VOTER")
                .requestMatchers("/mslr/**").permitAll()
                .anyRequest().authenticated()
        );


        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));


        http.formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {

                    if (authentication.getAuthorities()
                            .contains(new SimpleGrantedAuthority("ROLE_EC"))) {
                        response.sendRedirect("/ec");
                    } else {
                        response.sendRedirect("/voter");
                    }
                })
                .failureUrl("/login?error")
                .permitAll()
        );

        http.logout(logout -> logout.logoutSuccessUrl("/login?logout"));

        return http.build();
    }
}

