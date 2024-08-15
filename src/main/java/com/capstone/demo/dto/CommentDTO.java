package com.capstone.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDTO {
    private String id;
    private String articleId;
    private String parentId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String authorId;
    private String authorNickname;

    public CommentDTO(String id, String articleId, String parentId, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, String authorId, String authorNickname) {
        this.id = id;
        this.articleId = articleId;
        this.parentId = parentId;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
    }
}
