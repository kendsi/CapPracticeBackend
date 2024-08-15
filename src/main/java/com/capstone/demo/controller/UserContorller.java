package com.capstone.demo.controller;

import com.capstone.demo.dto.UserDTO;
import com.capstone.demo.security.CustomUserDetails;
import com.capstone.demo.service.UserService;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/users")
public class UserContorller {

    @Autowired
    private UserService userService;
    
    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userList = userService.getAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId) {
        UserDTO userData = userService.getUserById(userId);
        return new ResponseEntity<>(userData, HttpStatus.OK);
    }

    @GetMapping("/myinfo")
    public ResponseEntity<UserDTO> getMyInfo(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserDTO userData = userService.getUserById(userDetails.getUserId());
        return new ResponseEntity<>(userData, HttpStatus.OK);
    }
}
