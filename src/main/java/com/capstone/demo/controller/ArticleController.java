package com.capstone.demo.controller;

import com.capstone.demo.dto.ArticleRequestDTO;
import com.capstone.demo.dto.ArticleResponseDTO;
import com.capstone.demo.security.CustomUserDetails;
import com.capstone.demo.service.ArticleService;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    ArticleService articleService;


    
    @PostMapping("")
    public ResponseEntity<ArticleResponseDTO> createArticle(@ModelAttribute ArticleRequestDTO articleData, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        articleData.setAuthorId(userDetails.getUserId());
        articleData.setAuthorNickname(userDetails.getNickname());

        ArticleResponseDTO newArticle = articleService.createArticle(articleData);
        return new ResponseEntity<>(newArticle, HttpStatus.CREATED);
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<ArticleResponseDTO> updateArticle(@PathVariable String articleId, @ModelAttribute ArticleRequestDTO articleData, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        articleData.setAuthorId(userDetails.getUserId());
        
        ArticleResponseDTO updatedArticle = articleService.updateArticle(articleId, articleData);
        return new ResponseEntity<>(updatedArticle, HttpStatus.OK);
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<String> deleteArticle(@PathVariable String articleId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        articleService.deleteArticle(articleId, userDetails.getUserId());
        return new ResponseEntity<>("게시글 삭제 성공", HttpStatus.NO_CONTENT);
    }

    @GetMapping("")
    public ResponseEntity<List<ArticleResponseDTO>> getAllArticles() {
        List<ArticleResponseDTO> articleList = articleService.getAllArticles();
        return new ResponseEntity<>(articleList, HttpStatus.OK);
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleResponseDTO> getArticleById(@PathVariable String articleId) {
        ArticleResponseDTO article = articleService.getArticleById(articleId);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }
    
    @PostMapping("/search/author")
    public ResponseEntity<List<ArticleResponseDTO>> searchArticleByAuthor(@RequestParam String author) {
        List<ArticleResponseDTO> searchedList = articleService.getArticleByAuthorNickname(author);
        return new ResponseEntity<>(searchedList, HttpStatus.OK);
    }
    
    @PostMapping("/search/title")
    public ResponseEntity<List<ArticleResponseDTO>> searchArticleByTitle(@RequestParam String title) {
        List<ArticleResponseDTO> searchedList = articleService.getArticleByTitle(title);
        return new ResponseEntity<>(searchedList, HttpStatus.OK);
    }
}
