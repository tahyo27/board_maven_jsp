package com.duck.myboard.request;

import com.duck.myboard.domain.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardEdit {

    private Long id;
    private String title;
    private String content;


    @Builder
    public BoardEdit(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Board convert(BoardEdit boardEdit) {
        return Board.builder()
                .id(boardEdit.getId())
                .title(boardEdit.getTitle())
                .content(boardEdit.getContent())
                .build();
    }
}
