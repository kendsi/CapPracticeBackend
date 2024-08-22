package com.capstone.demo.service;

import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.demo.dto.CommentDTO;
import com.capstone.demo.exception.ChildCommentExistException;
import com.capstone.demo.exception.CommentNotFoundException;
import com.capstone.demo.exception.UnauthorizedException;
import com.capstone.demo.model.Comment;
import com.capstone.demo.repository.CommentRepo;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepo commentRepo;

    public CommentDTO createComment(CommentDTO commentData) {
        if (!commentData.getParentId().isEmpty() || commentData.getParentId() != "") {
            commentRepo.findById(commentData.getParentId()).orElseThrow(() -> new CommentNotFoundException("Parent comment not found with ID: " + commentData.getParentId()));
        }

        Comment comment = Comment.builder()
                                    .articleId(commentData.getArticleId())
                                    .parentId(commentData.getParentId())
                                    .content(commentData.getContent())
                                    .createdAt(LocalDateTime.now())
                                    .modifiedAt(LocalDateTime.now())
                                    .authorId(commentData.getAuthorId())
                                    .authorNickname(commentData.getAuthorNickname())
                                    .build();
        
        return convertToDTO(commentRepo.save(comment));
    }

    public CommentDTO updateComment(String commentId, CommentDTO commentData) {
        Comment comment = commentRepo.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + commentId));
        if (!commentData.getAuthorId().equals(comment.getAuthorId())) {
            throw new UnauthorizedException("Unauthorized to edit: not the comment owner.");
        }
        
        comment.setContent(commentData.getContent());
        comment.setModifiedAt(LocalDateTime.now());

        return convertToDTO(commentRepo.save(comment));
    }

    public void deleteComment(String commentId, String authorId) {
        Comment comment = commentRepo.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + commentId));
        if (!authorId.equals(comment.getAuthorId())) {
            throw new UnauthorizedException("Unauthorized to delete: not the comment owner.");
        }

        Optional<List<Comment>> childComments = commentRepo.findByParentId(commentId);
        if(childComments.isPresent() && !childComments.get().isEmpty()) {
            comment.setContent(null);
            comment.setCreatedAt(null);
            comment.setModifiedAt(null);
            comment.setAuthorId(null);
            comment.setAuthorNickname(null);
            commentRepo.save(comment);
            throw new ChildCommentExistException("해당 댓글에 답글이 달려있어 내용만 지워집니다.");
        }

        commentRepo.delete(comment);
    }

    public void deleteCommentByArticleId(String articleId) {
        commentRepo.deleteByArticleId(articleId);
    }

    public List<CommentDTO> getAllComments(String articleId) {
        List<Comment> comments = commentRepo.findByArticleId(articleId).orElseThrow(() -> new CommentNotFoundException("Comment not found with article ID: " + articleId));
        return comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CommentDTO getCommentById(String commentId) {
        Comment comment = commentRepo.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + commentId));
        return convertToDTO(comment);
    }

    private CommentDTO convertToDTO(Comment comment) {
        return CommentDTO.builder()
                            .id(comment.getId())
                            .articleId(comment.getArticleId())
                            .parentId(comment.getParentId())
                            .content(comment.getContent())
                            .createdAt(LocalDateTime.now())
                            .modifiedAt(LocalDateTime.now())
                            .authorId(comment.getAuthorId())
                            .authorNickname(comment.getAuthorNickname())
                            .build();
    }
}
