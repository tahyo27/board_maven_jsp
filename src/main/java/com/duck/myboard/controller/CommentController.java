package com.duck.myboard.controller;

import com.duck.myboard.request.CommentsRequest;
import com.duck.myboard.service.CommentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentsService commentsService;

    @PostMapping("/boards/{boardId}/comments")
    public void write(@PathVariable(value = "boardId") Long boardId, @ModelAttribute CommentsRequest commentsRequest) {

    }
}
