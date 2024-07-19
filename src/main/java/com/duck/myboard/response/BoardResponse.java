package com.duck.myboard.response;


import com.duck.myboard.domain.Board;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class BoardResponse {
    private String title;
    private String content;
    private String author;
    private String createAt;
    private long count;

    @Builder
    public BoardResponse(String title, String content, String author, String createAt, long count) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createAt = createAt;
        this.count = count;
    }

    public static BoardResponse convert(Board board) {
        String createAt = board.getCreateAt()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return BoardResponse.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .author(board.getAuthor())
                .createAt(createAt)
                .count(board.getCount())
                .build();
    }
}
