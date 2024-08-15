package com.capstone.demo.controller;

import com.capstone.demo.dto.ArticleDTO;
import com.capstone.demo.security.CustomUserDetails;
import com.capstone.demo.service.ArticleService;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    ArticleService articleService;
    
    @PostMapping("")
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody ArticleDTO articleData, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        articleData.setAuthorId(userDetails.getUserId());
        articleData.setAuthorNickname(userDetails.getNickname());

        ArticleDTO newArticle = articleService.createArticle(articleData);
        return new ResponseEntity<>(newArticle, HttpStatus.CREATED);
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable String articleId, @RequestBody ArticleDTO articleData, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        articleData.setAuthorId(userDetails.getUserId());
        
        ArticleDTO updatedArticle = articleService.updateArticle(articleId, articleData);
        return new ResponseEntity<>(updatedArticle, HttpStatus.OK);
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<String> deleteArticle(@PathVariable String articleId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        articleService.deleteArticle(articleId, userDetails.getUserId());
        return new ResponseEntity<>("게시글 삭제 성공", HttpStatus.NO_CONTENT);
    }

    @GetMapping("")
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        List<ArticleDTO> articleList = articleService.getAllArticles();
        return new ResponseEntity<>(articleList, HttpStatus.OK);
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable String articleId) {
        ArticleDTO article = articleService.getArticleById(articleId);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }
    
    @PostMapping("/search/author")
    public ResponseEntity<List<ArticleDTO>> searchArticleByAuthor(@RequestParam String author) {
        List<ArticleDTO> searchedList = articleService.getArticleByAuthorNickname(author);
        return new ResponseEntity<>(searchedList, HttpStatus.OK);
    }
    
    @PostMapping("/search/title")
    public ResponseEntity<List<ArticleDTO>> searchArticleByTitle(@RequestParam String title) {
        List<ArticleDTO> searchedList = articleService.getArticleByTitle(title);
        return new ResponseEntity<>(searchedList, HttpStatus.OK);
    }
}
