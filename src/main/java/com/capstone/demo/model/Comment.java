package com.capstone.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Document(collection = "Comments")
public class Comment {
    @Id
    private String id;
    private String articleId;
    private String parentId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String authorId;
    private String authorNickname;
}
