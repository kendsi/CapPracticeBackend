package com.capstone.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.capstone.demo.exception.UserNotFoundException;
import com.capstone.demo.model.User;
import com.capstone.demo.repository.UserRepo;
import com.capstone.demo.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepo userRepo;

    @Override
    public CustomUserDetails loadUserByUsername(String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        return new CustomUserDetails(user);
    }
}
