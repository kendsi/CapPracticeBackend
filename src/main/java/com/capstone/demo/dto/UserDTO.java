package com.capstone.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String userId;
    private String name;
    private String password;

    public UserDTO(String userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
    }
}
