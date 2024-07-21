package com.duck.myboard.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class Board {
    private Long id;
    private String title;
    private String content;
    private String author;
    private final long count;
    private String imageName;
    private final LocalDateTime createAt;

    @Builder
    public Board(Long id, String title, String content, String author, String imageName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.count = 0L;
        this.createAt = LocalDateTime.now();
        this.imageName = imageName;
    }
}
