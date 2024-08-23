package com.capstone.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.demo.dto.ArticleRequestDTO;
import com.capstone.demo.dto.ArticleResponseDTO;
import com.capstone.demo.exception.ArticleNotFoundException;
import com.capstone.demo.exception.ImageStorageException;
import com.capstone.demo.exception.InvalidFileFormatException;
import com.capstone.demo.exception.UnauthorizedException;
import com.capstone.demo.model.Article;
import com.capstone.demo.repository.ArticleRepo;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepo articleRepo;

    @Autowired
    private CommentService commentService;

    public ArticleResponseDTO createArticle(ArticleRequestDTO articleData) {

        List<String> imagePaths = uploadImages(articleData.getImages());

        Article article = Article.builder()
                                    .title(articleData.getTitle())
                                    .content(articleData.getContent())
                                    .createdAt(LocalDateTime.now())
                                    .modifiedAt(LocalDateTime.now())
                                    .authorId(articleData.getAuthorId())
                                    .authorNickname(articleData.getAuthorNickname())
                                    .imagePaths(imagePaths)
                                    .build();
        
        return convertToDTO(articleRepo.save(article));
    }

    public ArticleResponseDTO updateArticle(String articleId, ArticleRequestDTO articleData) {
        Article article = articleRepo.findById(articleId).orElseThrow(() -> new ArticleNotFoundException("Article not found with ID: " + articleId));
        if (!articleData.getAuthorId().equals(article.getAuthorId())) {
            throw new UnauthorizedException("Unauthorized to edit: not the article owner.");
        }

        List<String> imagePaths = article.getImagePaths();
        if (articleData.getImages() != null && !articleData.getImages().isEmpty()) {
            imagePaths = uploadImages(articleData.getImages());
        }

        article.setTitle(articleData.getTitle());
        article.setContent(articleData.getContent());
        article.setModifiedAt(LocalDateTime.now());
        article.setImagePaths(imagePaths);
        
        return convertToDTO(articleRepo.save(article));
    }

    public void deleteArticle(String articleId, String authorId) {
        Article article = articleRepo.findById(articleId).orElseThrow(() -> new ArticleNotFoundException("Article not found with ID: " + articleId));
        if (!authorId.equals(article.getAuthorId())) {
            throw new UnauthorizedException("Unauthorized to delete: not the article owner.");
        }

        commentService.deleteCommentByArticleId(articleId);
        articleRepo.delete(article);
    }

    public List<ArticleResponseDTO> getAllArticles() {
        List<Article> articles = articleRepo.findAll();
        return articles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ArticleResponseDTO getArticleById(String articleId) {
        Article article = articleRepo.findById(articleId).orElseThrow(() -> new ArticleNotFoundException("Article not found with ID: " + articleId));
        return convertToDTO(article);
    }

    public List<ArticleResponseDTO> getArticleByTitle(String title) {
        List<Article> articles = articleRepo.findByTitle(title).orElseThrow(() -> new ArticleNotFoundException("Article not found with title: " + title));
        return articles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ArticleResponseDTO> getArticleByAuthorNickname(String authorNickname) {
        List<Article> articles = articleRepo.findByAuthorNickname(authorNickname).orElseThrow(() -> new ArticleNotFoundException("Article not found with author: " + authorNickname));
        return articles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ArticleResponseDTO convertToDTO(Article article) {
        return ArticleResponseDTO.builder()
                                    .id(article.getId())
                                    .title(article.getTitle())
                                    .content(article.getContent())
                                    .createdAt(article.getCreatedAt())
                                    .modifiedAt(article.getModifiedAt())
                                    .authorId(article.getAuthorId())
                                    .authorNickname(article.getAuthorNickname())
                                    .imagePaths(article.getImagePaths())
                                    .build();
    }

    private List<String> uploadImages(List<MultipartFile> files) {
        
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");
        List<String> imagePaths = new ArrayList<>();

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {

                    String originalFilename = file.getOriginalFilename();
                    String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase() : "";
                    
                    if(!allowedExtensions.contains(fileExtension)) {
                        throw new InvalidFileFormatException("Invalid file format: " + fileExtension);
                    }

                    try {
                        String fileName = UUID.randomUUID().toString() + "." + fileExtension;
                        String userHome = System.getProperty("user.home");

                        String dirPath = userHome + "/images";

                        File dir = new File(dirPath);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }

                        String filePath = "images/" + fileName;

                        file.transferTo(new File(filePath));
                        imagePaths.add(filePath);
                    } catch (IOException e) {
                        throw new ImageStorageException("Failed to save image file");
                    }
                }
            }
        }

        return imagePaths;
    }
}
