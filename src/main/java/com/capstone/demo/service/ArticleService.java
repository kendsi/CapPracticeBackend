package com.capstone.demo.service;

import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.demo.dto.ArticleDTO;
import com.capstone.demo.exception.ArticleNotFoundException;
import com.capstone.demo.exception.UnauthorizedException;
import com.capstone.demo.model.Article;
import com.capstone.demo.repository.ArticleRepo;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepo articleRepo;

    public ArticleDTO createArticle(ArticleDTO articleData) {
        Article article = Article.builder()
                                    .title(articleData.getTitle())
                                    .content(articleData.getContent())
                                    .createdAt(LocalDateTime.now())
                                    .modifiedAt(LocalDateTime.now())
                                    .authorId(articleData.getAuthorId())
                                    .authorNickname(articleData.getAuthorNickname())
                                    .build();
        
        return convertToDTO(articleRepo.save(article));
    }

    public ArticleDTO updateArticle(String articleId, ArticleDTO articleData) {
        Article article = articleRepo.findById(articleId).orElseThrow(() -> new ArticleNotFoundException("Article not found with ID: " + articleId));
        if (!articleData.getAuthorId().equals(article.getAuthorId())) {
            throw new UnauthorizedException("Unauthorized to edit: not the article owner.");
        }

        article.setTitle(articleData.getTitle());
        article.setContent(articleData.getContent());
        article.setModifiedAt(LocalDateTime.now());
        
        return convertToDTO(articleRepo.save(article));
    }

    public void deleteArticle(String articleId, String authorId) {
        Article article = articleRepo.findById(articleId).orElseThrow(() -> new ArticleNotFoundException("Article not found with ID: " + articleId));
        if (!authorId.equals(article.getAuthorId())) {
            throw new UnauthorizedException("Unauthorized to delete: not the article owner.");
        }

        articleRepo.delete(article);
    }

    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = articleRepo.findAll();
        return articles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ArticleDTO getArticleById(String articleId) {
        Article article = articleRepo.findById(articleId).orElseThrow(() -> new ArticleNotFoundException("Article not found with ID: " + articleId));
        return convertToDTO(article);
    }

    public List<ArticleDTO> getArticleByTitle(String title) {
        List<Article> articles = articleRepo.findByTitle(title).orElseThrow(() -> new ArticleNotFoundException("Article not found with title: " + title));
        return articles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ArticleDTO> getArticleByAuthorNickname(String authorNickname) {
        List<Article> articles = articleRepo.findByAuthorNickname(authorNickname).orElseThrow(() -> new ArticleNotFoundException("Article not found with author: " + authorNickname));
        return articles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ArticleDTO convertToDTO(Article article) {
        return new ArticleDTO(article.getId(), article.getTitle(), article.getContent(), article.getCreatedAt(), article.getModifiedAt(), article.getAuthorId(), article.getAuthorNickname());
    }
}
