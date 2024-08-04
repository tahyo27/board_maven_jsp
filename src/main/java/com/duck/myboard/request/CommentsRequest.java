package com.duck.myboard.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentsRequest {
    private Long id;
    private String author;
    private String content;

    @Builder
    public CommentsRequest(Long id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }
}
