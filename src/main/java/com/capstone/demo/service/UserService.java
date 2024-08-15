package com.capstone.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.capstone.demo.dto.UserDTO;
import com.capstone.demo.exception.InvalidUsernameException;
import com.capstone.demo.exception.UserNotFoundException;
import com.capstone.demo.model.User;
import com.capstone.demo.repository.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO createUser(UserDTO userData) {
        if (userRepo.findByUsername(userData.getUsername()).isPresent()) {
            throw new InvalidUsernameException("User already exists with username: " + userData.getUsername());
        }

        User user = User.builder()
                .username(userData.getUsername())
                .nickname(userData.getNickname())
                .password(passwordEncoder.encode(userData.getPassword()))
                .build();

        return convertToDTO(userRepo.save(user));
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepo.findAll();
        return users.stream()
                .map(this::convertToDTO)  // 각 User를 UserDTO로 변환
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        return convertToDTO(user);
    }

    public UserDTO getUserByNickname(String nickname) {
        User user = userRepo.findByNickname(nickname).orElseThrow(() -> new UserNotFoundException("User not found with nickname: " + nickname));
        return convertToDTO(user);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getNickname(), user.getPassword());
    }
}
