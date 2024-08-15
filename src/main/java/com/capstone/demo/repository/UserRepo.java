package com.capstone.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.capstone.demo.model.User;
import java.util.Optional;

public interface UserRepo extends MongoRepository<User, String>{
    Optional<User> findByUsername(String username);
    Optional<User> findByNickname(String nickname);
}
