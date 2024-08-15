package com.capstone.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.capstone.demo.dto.UserDTO;
import com.capstone.demo.exception.InvalidPasswordException;
import com.capstone.demo.exception.InvalidUsernameException;
import com.capstone.demo.security.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    public UserDTO signup(UserDTO userData) {
        if (userData.getUsername().isEmpty() || userData.getUsername() == "") {
            throw new InvalidUsernameException("Username must be at least one character long.");
        }
        else if (userData.getPassword().isEmpty() || userData.getPassword() == "") {
            throw new InvalidPasswordException("Password must be at least one character long.");
        }

        return userService.createUser(userData);
    }

    public String login(String username, String password) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );

        return jwtTokenProvider.generateToken(authentication);
    }

    public void logout(HttpServletRequest request) {
        String token = jwtTokenProvider.getTokenFromRequest(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            jwtTokenProvider.addToBlacklist(token);
        }
    }
}
