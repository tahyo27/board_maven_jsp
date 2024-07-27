package com.duck.myboard.response;


import com.duck.myboard.domain.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@Getter
@ToString
public class BoardResponse {

    private Long id;
    private String title;
    private String content;
    private String author;
    private String createAt;
    private long count;

    @Builder
    public BoardResponse(Long id, String title, String content, String author, String createAt, long count) {
        this.id = id;
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
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .author(board.getAuthor())
                .createAt(createAt)
                .count(board.getCount())
                .build();
    }
}
