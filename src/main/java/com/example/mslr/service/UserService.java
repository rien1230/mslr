package com.example.mslr.service;

import com.example.mslr.controller.RegisterForm;
import com.example.mslr.model.Role;
import com.example.mslr.model.User;
import com.example.mslr.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public User registerVoter(RegisterForm form) {
        User user = new User();
        user.setEmail(form.getEmail());
        user.setFullName(form.getFullName());
        user.setDob(form.getDob());
        user.setRole(Role.VOTER);

        user.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        return userRepository.save(user);
    }
}

