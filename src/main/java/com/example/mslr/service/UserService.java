package com.example.mslr.service;

import com.example.mslr.repo.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
 private final  UserRepository userRepository;
 public UserService(UserRepository userRepository){
     this.userRepository= userRepository;
 }
}
