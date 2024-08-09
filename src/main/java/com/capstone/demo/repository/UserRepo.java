package com.capstone.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import com.capstone.demo.model.User;

public interface UserRepo extends MongoRepository<User, String>{
    Optional<User> findByName(String name);
}
