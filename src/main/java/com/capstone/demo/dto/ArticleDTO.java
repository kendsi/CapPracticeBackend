package com.capstone.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleDTO {
    private String id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String authorId;
    private String authorNickname;

    public ArticleDTO(String id, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, String authorId, String authorNickname) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
    }
}