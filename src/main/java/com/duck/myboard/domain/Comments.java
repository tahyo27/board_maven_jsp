package com.duck.myboard.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class Comments {
    private Long id;
    private String author;
    private String content;
    private LocalDateTime createAt;
    private Long parentId;
    private Long boardId;

    @Builder
    public Comments(Long id, String author, String content, Long parentId, Long boardId) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.createAt = LocalDateTime.now();
        this.parentId = parentId;
        this.boardId = boardId;
    }
}
