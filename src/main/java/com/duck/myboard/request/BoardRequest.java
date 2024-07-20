package com.duck.myboard.request;

import com.duck.myboard.domain.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class BoardRequest {

    private String title;
    private String content;
    private String author;

    @Builder
    public BoardRequest(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public static Board createConvert (BoardRequest boardRequest) {
        return Board.builder()
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .author(boardRequest.getAuthor())
                .build();
    }

    public static Board editConvert (Long boardId, BoardRequest boardRequest) {
        return Board.builder()
                .id(boardId)
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .build();
    }
}
