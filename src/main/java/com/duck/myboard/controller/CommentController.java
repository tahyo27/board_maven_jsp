package com.duck.myboard.controller;

import com.duck.myboard.domain.Comments;
import com.duck.myboard.request.CommentsRequest;
import com.duck.myboard.service.CommentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentsService commentsService;

    @PostMapping("/boards/{boardId}/comments")
    public void writeComments(@PathVariable(value = "boardId") Long boardId, @RequestBody CommentsRequest commentsRequest) {
        commentsService.write(boardId, commentsRequest);
    }

    @GetMapping("/boards/{boardsId}/comments")
    public List<Comments> getCommentsList(@PathVariable(value = "boardId") Long boardId) {
        return commentsService.getList();
    }
}
