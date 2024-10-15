package com.duck.myboard.domain;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
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
