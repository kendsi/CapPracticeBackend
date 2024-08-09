package com.capstone.demo.model;

import org.springframework.data.annotation.Id;
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
    private String userId;
    private String name;
    private String password;
}
