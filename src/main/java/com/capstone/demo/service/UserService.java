package com.capstone.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.demo.dto.UserDTO;
import com.capstone.demo.model.User;
import com.capstone.demo.repository.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public UserDTO createUser(UserDTO userData) {
        User newUser = User.builder()
            .userId(userData.getUserId())
            .name(userData.getName())
            .password(userData.getPassword())
            .build();

        userRepo.save(newUser);
        return userData;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepo.findAll();
        return users.stream()
                .map(this::convertToDTO)  // 각 User를 UserDTO로 변환
                .collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getUserId(), user.getName(), user.getPassword());
    }
}
