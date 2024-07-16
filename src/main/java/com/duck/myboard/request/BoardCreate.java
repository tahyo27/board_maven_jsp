package com.duck.myboard.request;


import com.duck.myboard.domain.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardCreate {

    private String title;
    private String content;
    private String author;

    @Builder
    public BoardCreate(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Board convert(BoardCreate boardCreate) {
        return Board.builder()
                .title(boardCreate.getTitle())
                .content(boardCreate.getContent())
                .author(boardCreate.getAuthor())
                .build();
    }
}
