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
    private Long boardId;
    private Long parentId; // 임시
    private String author;
    private String content;


    @Builder
    public CommentsRequest(Long id, String author, String content, Long boardId) {
        this.id = id;
        this.boardId = boardId;
        this.author = author;
        this.content = content;
    }

    public static Comments createComments(CommentsRequest commentsRequest) {
        return Comments.builder()
                .boardId(commentsRequest.getBoardId())
                .parentId(commentsRequest.getParentId())
                .author(commentsRequest.getAuthor())
                .content(commentsRequest.getContent())
                .build();
    }
}
