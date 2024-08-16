package com.capstone.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
public class ArticleRequestDTO {
    private String title;
    private String content;
    private String authorId;
    private String authorNickname;

    private List<MultipartFile> images;
}