package com.capstone.demo.controller;

import com.capstone.demo.dto.CommentDTO;
import com.capstone.demo.security.CustomUserDetails;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.capstone.demo.service.CommentService;

@RestController
@RequestMapping("/api/articles/{articleId}/comments")
public class CommentController {
    
    @Autowired
    private CommentService commentService;

    @PostMapping("")
    public ResponseEntity<CommentDTO> createComment(@PathVariable String articleId, @RequestBody CommentDTO commentData, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        commentData.setArticleId(articleId);
        commentData.setAuthorId(userDetails.getUserId());
        commentData.setAuthorNickname(userDetails.getNickname());

        CommentDTO newComment = commentService.createComment(commentData);
        return new ResponseEntity<>(newComment, HttpStatus.OK);
    }
    
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable String articleId, @PathVariable String commentId, @RequestBody CommentDTO commentData, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        commentData.setAuthorId(userDetails.getUserId());
        
        CommentDTO updatedComment = commentService.updateComment(commentId, commentData);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }
    
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable String articleId, @PathVariable String commentId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        
        commentService.deleteComment(commentId, userDetails.getUserId());
        return new ResponseEntity<>("댓글 삭제 성공", HttpStatus.NO_CONTENT);
    }

    @GetMapping("")
    public ResponseEntity<List<CommentDTO>> getAllComments(@PathVariable String articleId) {
        List<CommentDTO> commentList = commentService.getAllComments(articleId);
        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable String articleId, @PathVariable String commentId) {
        CommentDTO comment = commentService.getCommentById(commentId);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }
    
}
