package com.example.mslr.security;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.example.mslr.model.User;
import com.example.mslr.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DbUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user with email: " + email));

        String role = "ROLE_" + u.getRole().name();

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())
                .password(u.getPasswordHash())
                .authorities(List.of(new SimpleGrantedAuthority(role)))
                .build();
    }
}
