package com.capstone.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.capstone.demo.model.Comment;

import java.util.Optional;
import java.util.List;

public interface CommentRepo extends MongoRepository<Comment, String> {
    Optional<Comment> findByAuthorNickname(String authorNickname);
    Optional<List<Comment>> findByParentId(String parentId);
    Optional<List<Comment>> findByArticleId(String articleId);
    void deleteByArticleId(String articleId);
}
