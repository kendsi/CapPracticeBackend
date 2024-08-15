package com.capstone.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.capstone.demo.model.Article;

import java.util.Optional;
import java.util.List;

public interface ArticleRepo extends MongoRepository<Article, String> {
    Optional<List<Article>> findByTitle(String title);
    Optional<List<Article>> findByAuthorNickname(String authorNickname);
    Optional<List<Article>> findByAuthorId(String authorId);
}
