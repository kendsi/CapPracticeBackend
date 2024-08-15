package com.capstone.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
@Document(collection = "Users")
public class User {
    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    private String nickname;
    private String password;
}
