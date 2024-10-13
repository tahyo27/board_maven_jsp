package com.duck.myboard.request;

import com.duck.myboard.domain.Comments;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentsRequest {
    private Long id;
    private Long parentId; // 임시
    private String author;
    private String content;


    @Builder
    public CommentsRequest(Long id, String author, String content, Long parentId) {
        this.id = id;
        this.parentId = parentId;
        this.author = author;
        this.content = content;
    }

    public static Comments createComments(Long boardId, CommentsRequest commentsRequest) {
        return Comments.builder()
                .boardId(boardId)
                .parentId(commentsRequest.getParentId())
                .author(commentsRequest.getAuthor())
                .content(commentsRequest.getContent())
                .build();
    }
}
